package gebd.games.boat.scene;

import composites.entities.Entity;
import gebd.Render;
import gebd.games.boat.BoatVis;
import gebd.games.boat.entity.pinger.PingerHandler;
import gebd.games.boat.json.util.Vector3fJson;
import gebd.games.boat.ui.topnav.file.util.ResourceUtils;
import loader.LoadedObjectHandler;
import org.json.simple.JSONObject;
import javax.vecmath.Vector3f;
import java.io.File;

/**
 * Created by CaptainPete on 7/28/2016.
 */
public class SceneObject implements StoresInSceneFile {

    private Entity sceneEntity;
    private BoatVisScene parent;

    private static final String MODEL_PATH_ATTRIBUTE_NAME = "model_path";
    private static final String TEXTURE_PATH_ATTRIBUTE_NAME = "texture_path";
    private static final String POSITION_ATTRIBUTE_NAME = "position";
    private static final String ROTATION_ATTRIBUTE_NAME = "rotation";
    private static final String SIZE_ATTRIBUTE_NAME = "size";
    private static final String BLEND_COLOUR_ATTRIBUTE_NAME = "blend_colour";
    private static final String BLEND_AMOUNT_ATTRIBUTE_NAME = "blend_amount";
    private static final String USE_COLOUR_SEQUENCE = "colour_sequence_enabled";
    private static final String PINGING_FREQUENCY = "pinging_frequency";

    public SceneObject() {

    }

    public SceneObject(Entity entity) {
        this.sceneEntity = entity;
    }

    public void setupEntity() {
        if (sceneEntity.getLoadStatus() == null) {
            LoadedObjectHandler.load(sceneEntity);
        }
    }

    @Override
    public void storeInformation(JSONObject object) {

        storePathAsRelative(object, MODEL_PATH_ATTRIBUTE_NAME, sceneEntity.getModel().getModelPath());
        storePathAsRelative(object, TEXTURE_PATH_ATTRIBUTE_NAME, sceneEntity.getTexture().getPath());
        Vector3fJson.storeVector3fOrNoneIfDefault(sceneEntity.getPosition(), object, POSITION_ATTRIBUTE_NAME, Entity.DEFAULT_POSITION);
        Vector3fJson.storeVector3fOrNoneIfDefault(sceneEntity.getRotation(), object, ROTATION_ATTRIBUTE_NAME, Entity.DEFAULT_ROTATION);
        Vector3fJson.storeVector3fOrNoneIfDefault(sceneEntity.getSize(), object, SIZE_ATTRIBUTE_NAME, Entity.DEFAULT_SIZE);
        Vector3fJson.storeVector3fOrNoneIfDefault(sceneEntity.getTextureBlendColour(), object, BLEND_COLOUR_ATTRIBUTE_NAME, Entity.DEFAULT_BLEND_COLOUR);
        Vector3fJson.storeFloatOrNoneIfDefault(sceneEntity.getTextureBlendAmount(), object, BLEND_AMOUNT_ATTRIBUTE_NAME, Entity.DEFAULT_BLEND_AMOUNT);

        BoatVis boatVis = (BoatVis) BoatVis.instance;
        if (boatVis.getColourSequenceHandler().contains(sceneEntity)) {
            object.put(USE_COLOUR_SEQUENCE, "true");
        }

        if (PingerHandler.contains(sceneEntity)) {
            object.put(PINGING_FREQUENCY, "" + PingerHandler.getPingingFrequency(sceneEntity));
        }
    }

    private void storePathAsRelative(JSONObject object, String keyName, String absolutePath) {
        String base = (new File(".")).getAbsolutePath();
        //String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
        String relative = absolutePath;

        try {
            relative = ResourceUtils.getRelativePath(absolutePath, base, "/");
        } catch (RuntimeException e) {
            //Unable to convert path... Assumed already a relative path.
        }

        object.put(keyName, relative);
    }

    @Override
    public void loadInformation(JSONObject object) {
        String modelPath = (String) object.get(MODEL_PATH_ATTRIBUTE_NAME);
        String texturePath = (String) object.get(TEXTURE_PATH_ATTRIBUTE_NAME);
        Vector3f position = Vector3fJson.parseVector3fOrUseDefaults(object, POSITION_ATTRIBUTE_NAME, Entity.DEFAULT_POSITION);
        Vector3f rotation = Vector3fJson.parseVector3fOrUseDefaults(object, ROTATION_ATTRIBUTE_NAME, Entity.DEFAULT_ROTATION);
        Vector3f size = Vector3fJson.parseVector3fOrUseDefaults(object, SIZE_ATTRIBUTE_NAME, Entity.DEFAULT_SIZE);
        Vector3f blendColour = Vector3fJson.parseVector3fOrUseDefaults(object, BLEND_COLOUR_ATTRIBUTE_NAME, Entity.DEFAULT_BLEND_COLOUR);
        float colourBlendAmount = Vector3fJson.getFloatOrUseDefault(object, BLEND_AMOUNT_ATTRIBUTE_NAME, Entity.DEFAULT_BLEND_AMOUNT);
        sceneEntity = new Entity(modelPath, texturePath, position, size, rotation);
        sceneEntity.setTextureBlendColour(blendColour);
        sceneEntity.setTextureBlendAmount(colourBlendAmount);

        Object colSeqStringAsObj = object.get(USE_COLOUR_SEQUENCE);
        if ((colSeqStringAsObj != null) && (colSeqStringAsObj instanceof String)) {
            String useColourSequence = (String) colSeqStringAsObj;
            if ("true".equalsIgnoreCase(useColourSequence.trim())) {
                BoatVis boatVis = (BoatVis) BoatVis.instance;
                boatVis.getColourSequenceHandler().addColouredEntity(sceneEntity);
            }
        }

        Object entityFrequencyStringAsObj = object.get(PINGING_FREQUENCY);
        if ((entityFrequencyStringAsObj != null) && (entityFrequencyStringAsObj instanceof String)) {
            String pingingFrequencyAsString = (String) entityFrequencyStringAsObj;
            try {
                double frequency = Double.valueOf(pingingFrequencyAsString);
                PingerHandler.togglePingingBuoy(sceneEntity, frequency);
            } catch (NumberFormatException e) {
                //Invalid frequency, ignore.
            }
        }

    }

    public Entity getSceneEntity() {
        return sceneEntity;
    }

    public boolean isLoaded() {
        return sceneEntity.isLoaded();
    }
}
