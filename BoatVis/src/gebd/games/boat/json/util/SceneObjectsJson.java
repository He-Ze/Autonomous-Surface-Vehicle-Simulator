package gebd.games.boat.json.util;

import gebd.Render;
import gebd.games.boat.scene.SceneObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by P3TE on 1/08/2016.
 */
public class SceneObjectsJson {

    public static final String SCENE_OBJECTS_KEY = "scene_objects";

    public static void storeSceneObjects(JSONObject jsonObject, List<SceneObject> sceneObjects) {
        JSONArray sceneObjectsArray = new JSONArray();
        ListIterator<SceneObject> sceneObjectListIterator = sceneObjects.listIterator();
        while (sceneObjectListIterator.hasNext()) {
            SceneObject nextObject = sceneObjectListIterator.next();
            if (!Render.instance.entityInRenderList(nextObject.getSceneEntity())) {
                //Don't store the entity in the file.
                continue;
            }
            JSONObject jsonSceneObject = new JSONObject();
            nextObject.storeInformation(jsonSceneObject);
            sceneObjectsArray.add(jsonSceneObject);
        }
        jsonObject.put(SCENE_OBJECTS_KEY, sceneObjectsArray);
    }

    public static ArrayList<SceneObject> loadSceneObjectInformation(JSONObject jsonObject) {
        JSONArray sceneObjectsArray = (JSONArray) jsonObject.get(SCENE_OBJECTS_KEY);
        ArrayList<SceneObject> loadedSceneObjects = new ArrayList<>(sceneObjectsArray.size());
        for (int objectNo = 0; objectNo < sceneObjectsArray.size(); objectNo++) {
            JSONObject sceneObjectJson = (JSONObject) sceneObjectsArray.get(objectNo);
            SceneObject newSceneObject = new SceneObject();
            newSceneObject.loadInformation(sceneObjectJson);
            loadedSceneObjects.add(newSceneObject);
        }
        return loadedSceneObjects;
    }
}
