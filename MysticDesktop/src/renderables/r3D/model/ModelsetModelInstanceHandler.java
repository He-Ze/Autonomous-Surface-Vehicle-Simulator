package renderables.r3D.model;

import java.util.HashMap;

/**
 * Created by CaptainPete on 8/6/2016.
 */
public class ModelsetModelInstanceHandler {

    private static HashMap<String, ModelsetModel> loadedModels = new HashMap<>();

    public static <T extends ModelsetModel> T addNewModel(T modelsetModel) {
        loadedModels.put(modelsetModel.getModelPath(), modelsetModel);
        return modelsetModel;
    }

    public static ModelsetModel getModel(String entFilePath) {
        return loadedModels.get(entFilePath);
    }

}
