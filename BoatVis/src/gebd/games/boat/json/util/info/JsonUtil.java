package gebd.games.boat.json.util.info;

import org.json.simple.JSONObject;

/**
 * Created by p3te on 8/12/16.
 */
public class JsonUtil {

    public static void tryStore(JSONObject jsonObject, Object key, Object valueToStore, Object defaultValue) {
        if (valueToStore == null) {
            return; //Don't store anything.
        }
        if (valueToStore.equals(defaultValue)) {
            return; //It's already at the default value, don't need to store anything.
        }

        jsonObject.put(key, valueToStore);

    }

    public static void tryLoadIgnoreMissing(JSONObject jsonObject, Object key, TryLoadIgnoreMissingValue tryLoadJson) {

        tryLoadGeneric(jsonObject, key, tryLoadJson);

    }

    public static void tryLoad(JSONObject jsonObject, Object key, TryLoadJson tryLoadJson) {

        tryLoadGeneric(jsonObject, key, tryLoadJson);

    }

    private static void tryLoadGeneric(JSONObject jsonObject, Object key, TryLoadIgnoreMissingValue tryLoadJson) {
        try {

            Object loadedObject = jsonObject.get(key);
            if (loadedObject == null) {
                if (tryLoadJson instanceof TryLoadJson) {
                    //If handling the failure, handle it, otherwise ignore the failure.
                    ((TryLoadJson) tryLoadJson).onFailure();
                }
            } else {
                tryLoadJson.onSuccess(loadedObject);
            }

        } catch (Exception e) {
            //Load failed for some reason.
        }
    }


}
