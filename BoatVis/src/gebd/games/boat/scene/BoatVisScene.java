package gebd.games.boat.scene;

import composites.entities.Entity;
import gebd.Render;
import gebd.games.boat.BoatVis;
import gebd.games.boat.json.util.ColourSequenceJson;
import gebd.games.boat.json.util.ParseNiceJson;
import gebd.games.boat.json.util.SceneObjectsJson;
import gebd.games.boat.json.util.SettingsHolderJson;
import gebd.games.boat.lidar.LidarHelper;
import gebd.games.boat.lidar.LidarReading;
import gebd.games.boat.physics.PhysicsSettings;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.settings.SettingsHolderWithParents;
import renderables.r3D.water.WaterSettings;

import java.io.*;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by CaptainPete on 7/28/2016.
 */
public class BoatVisScene implements StoresInSceneFile {

    private File sceneFilePath;
    SettingsHolderWithParents<WaterSettings> waterSettings = new SettingsHolderWithParents<>(WaterSettings.values());
    SettingsHolderWithParents<PhysicsSettings> physicsSettings = new SettingsHolderWithParents<>(PhysicsSettings.values());
    public static final String WATER_SETTINGS_KEY = "water_settings";
    public static final String PHYSICS_SETTINGS_KEY = "physics_settings";
    public static final String DEPENDANT_SCENES_KEY = "dependant_scenes";
    public static final String LIDAR_ROTATION_SPEED_KEY = "lidar_rotational_speed";
    private ArrayList<BoatVisScene> boatVisSceneDependancies = new ArrayList<>();
    private ArrayList<SceneObject> sceneObjects = new ArrayList<>();


    public void addNewSceneObject(Entity entity) {
        sceneObjects.add(new SceneObject(entity));
    }

    public void loadAllEntites() {
        //Load all the dependant scenes.
        for (BoatVisScene boatVisScene : boatVisSceneDependancies) {
            boatVisScene.loadAllEntites();
        }
        //Load all of the scene objects.
        for (SceneObject sceneObject : sceneObjects) {
            sceneObject.setupEntity();
        }
    }

    public void saveToFile(File file) throws IOException {
        JSONObject newJsonObject = new JSONObject();
        storeInformation(newJsonObject);
        FileWriter writer = null;
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            writer = new FileWriter(file);
            writer.write(ParseNiceJson.prettyParseJson(newJsonObject.toJSONString(), 2).toString());
            writer.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                //Can't close the writer, don't worry about it.
            }
        }
        //Set the scene file to the save file. (only on success)
        sceneFilePath = file;
    }

    public void loadFromFile(File file) throws IOException {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            StringBuilder stringBuilder = new StringBuilder();
            boolean reading = true;
            while (reading) {
                int nextByte = fileReader.read();
                if (nextByte == -1) {
                    reading = false;
                } else {
                    stringBuilder.append((char) nextByte);
                }
            }
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(stringBuilder.toString());
            loadInformation(jsonObject);
            //String fileContents = fileReader.
        } catch (IOException | ParseException e) {
            throw new IOException(e);
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                //Can't close the reader, don't worry about it.
            }
        }
        //Set the scene file to the loaded file. (only on success)
        sceneFilePath = file;
    }

    private void storeScenes(JSONObject jsonObject) {
        JSONArray jsonArray = new JSONArray();
        for (BoatVisScene dependantScene : boatVisSceneDependancies) {
            jsonArray.add(dependantScene.sceneFilePath);
        }
        jsonObject.put(DEPENDANT_SCENES_KEY, jsonArray);
    }

    private void loadScenes(JSONObject jsonObject) {
        JSONArray sceneArray = (JSONArray) jsonObject.get(DEPENDANT_SCENES_KEY);
        ListIterator<Object> sceneArrayIterator = sceneArray.listIterator();
        while (sceneArrayIterator.hasNext()) {
            String sceneFilePath = (String) sceneArrayIterator.next();
            BoatVisScene newScene = new BoatVisScene();
            File sceneFile = new File(sceneFilePath);
            try {
                newScene.loadFromFile(sceneFile);
            } catch (IOException e) {
                throw new RuntimeException("Invalid scene with given path: " + sceneFilePath, e);
            }
        }
    }

    @Override
    public void storeInformation(JSONObject object) {
        //Store the dependant scenes.
        storeScenes(object);

        //Store the water settings.
        SettingsHolderJson.storeInformation(object, WATER_SETTINGS_KEY, waterSettings);

        //Store the water settings.
        SettingsHolderJson.storeInformation(object, PHYSICS_SETTINGS_KEY, physicsSettings);

        //Store the lidar rotational speed.
        BoatVis boatVis = (BoatVis) Render.instance;
        object.put(LIDAR_ROTATION_SPEED_KEY, boatVis.getLidarHelper().getLidarRotationSpeed().name());

        //Store the current colour sequence.
        ColourSequenceJson.storeJSON(object, boatVis);

        //Store all of the scene objects.
        SceneObjectsJson.storeSceneObjects(object, sceneObjects);

    }

    @Override
    public void loadInformation(JSONObject object) {
        //Get the boatvis instance.
        BoatVis boatVis = (BoatVis) Render.instance;

        //Load the dependant scenes.
        loadScenes(object);

        //Load the water settings.
        JSONObject settingsHolderJsonObject = (JSONObject) object.get(WATER_SETTINGS_KEY);
        SettingsHolderJson.loadSettingsHolder(settingsHolderJsonObject, waterSettings);
        boatVis.getWaterSettingsDisplay().updateAllValues(waterSettings);

        //Store the physics settings.
        JSONObject physicsSettingsHolderJsonObject = (JSONObject) object.get(PHYSICS_SETTINGS_KEY);
        SettingsHolderJson.loadSettingsHolder(physicsSettingsHolderJsonObject, physicsSettings);

        //Load the Lidar rotational speed.
        Object lidarRotationalValue = object.get(LIDAR_ROTATION_SPEED_KEY);
        String rotationSpeedName = String.valueOf(lidarRotationalValue);
        for (int rotationSpeedOrdinal = 0; rotationSpeedOrdinal < LidarHelper.LidarRotationSpeed.values().length; rotationSpeedOrdinal++) {
            LidarHelper.LidarRotationSpeed lidarRotationSpeed = LidarHelper.LidarRotationSpeed.values()[rotationSpeedOrdinal];
            if (rotationSpeedName.equalsIgnoreCase(lidarRotationSpeed.name())) {
                boatVis.setLidarRotationSpeed(lidarRotationSpeed);
            }
        }


        //Load the colour sequence.
        ColourSequenceJson.loadJSON(object, boatVis);

        //Load all of the scene objects.
        sceneObjects = SceneObjectsJson.loadSceneObjectInformation(object);
    }

    public SettingsHolderWithParents<WaterSettings> getWaterSettings() {
        return waterSettings;
    }

    public SettingsHolderWithParents<PhysicsSettings> getPhysicsSettings() {
        return physicsSettings;
    }

    public File getFile() {
        return sceneFilePath;
    }

    public void setFile(File file) {
        this.sceneFilePath = file;
    }
}