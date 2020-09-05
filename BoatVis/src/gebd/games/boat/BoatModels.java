package gebd.games.boat;

import blindmystics.util.FileReader;
import gebd.ModelsetPlus;

/**
 * Created by alec on 29/11/15.
 */
public class BoatModels {
    private final static String baseDir = "res/boat/";

    public enum ModelName {
        GOLD_CREEK_LAKE_BED,
        GOLD_CREEK_TREELINE,
        WATER_WITH_CENTER_HOLE,
        BOAT,
        CUBE_TEST,
        QUAD,
        QUAD_UP,
        BRIDGE_TEST,
        CAMERA_PLACEHOLDER,
        BUOY_STANDARD,
        BUOY_TAPERED,
        CYLINDER,
        SHPERE,
        VELODYNE_HDE32E,
        UNIT_CYLINDER,
        WAMV_PHYS,
        LIGHT_BUOY,
        DOUBLE_SIDED_PLANE
    }

    private final static String[] NAMES = generateNames();

    private static String[] generateNames(){
        String[] modelNames = new String[ModelName.values().length];
        for (int modelNo = 0; modelNo < ModelName.values().length; modelNo++) {
            ModelName modelName = ModelName.values()[modelNo];
            modelNames[modelNo] = modelName.name();
        }
        return modelNames;
    }

    private final static String[] PATHS = new String[]{
            //baseDir + "entities/testBoat.ent",
            FileReader.asResource(baseDir + "entities/GoldcreekLakebed.ent"),
            FileReader.asResource(baseDir + "entities/Treeline.ent"),
            FileReader.asResource(baseDir + "entities/WaterWithCenterHole.ent"),
            FileReader.asResource(baseDir + "entities/WamV.ent"),
            FileReader.asResource(baseDir + "entities/cube.ent"),
            FileReader.asResource(baseDir + "entities/quad.ent"),
            FileReader.asResource(baseDir + "entities/quadUp.ent"),
            FileReader.asResource(baseDir + "entities/testBridge.ent"),
            FileReader.asResource(baseDir + "entities/cameraPlaceholder.ent"),
            FileReader.asResource(baseDir + "entities/buoyStandard.ent"),
            FileReader.asResource(baseDir + "entities/buoyTapered.ent"),
            FileReader.asResource(baseDir + "entities/cylinder.ent"),
            FileReader.asResource(baseDir + "entities/sphere.ent"),
            FileReader.asResource(baseDir + "entities/VelodyneHDE32E.ent"),
            FileReader.asResource(baseDir + "entities/uint_cylinder.ent"),
            FileReader.asResource(baseDir + "entities/WamV_Phys.ent"),
            FileReader.asResource(baseDir + "entities/LightBuoy.ent"),
            FileReader.asResource(baseDir + "entities/DoubleSidedPlane.ent"),
    };
    private final static String[] TEXTURES = new String[] {
            //baseDir + "boatTexture.png",
            FileReader.asResource(baseDir + "textures/GoldCreekLakeTexture.png"),
            FileReader.asResource(baseDir + "textures/TreeStarter.png"),
            FileReader.asSharedFile("test_images/numbered_cube.png"),
            FileReader.asResource(baseDir + "textures/WamVTexture.png"),
            FileReader.asSharedFile("test_images/numbered_cube.png"),
            FileReader.asSharedFile("test_images/numbered_cube.png"),
            FileReader.asSharedFile("test_images/numbered_cube.png"),
            FileReader.asResource(baseDir + "entities/bridge.png"),
            FileReader.asResource(baseDir + "textures/CamTexture.png"),
            FileReader.asResource(baseDir + "textures/simple/White.png"),
            FileReader.asResource(baseDir + "textures/simple/White.png"),
            FileReader.asResource(baseDir + "textures/simple/White.png"),
            FileReader.asResource(baseDir + "textures/simple/White.png"),
            FileReader.asResource(baseDir + "textures/VelodyneHDE32E_Texture.png"),
            FileReader.asResource(baseDir + "textures/simple/White.png"),
            FileReader.asResource(baseDir + "textures/WamVTexture.png"),
            FileReader.asSharedFile("shared/textures/default_texture.png"),
            FileReader.asSharedFile("shared/textures/default_texture.png")
    };

    private static ModelsetPlus instance = new ModelsetPlus(NAMES, PATHS, TEXTURES, null, null);

    public static ModelsetPlus getInstance() {
        //System.out.printf("Distance:%f\n",instance);

        return instance;
    }
}
