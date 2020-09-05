package gebd.games.boat.json.util;

import org.json.simple.JSONObject;
import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 7/28/2016.
 */
public class Vector3fJson {

    public static void storeVector3f(Vector3f vector3f, JSONObject jsonObject, String name) {
        JSONObject storedVector = new JSONObject();
        storedVector.put("x", vector3f.x);
        storedVector.put("y", vector3f.y);
        storedVector.put("z", vector3f.z);
        jsonObject.put(name, storedVector);
    }

    public static void storeVector3fOrNoneIfDefault(Vector3f vector3f, JSONObject jsonObject, String name, Vector3f defaultValues) {
        if (defaultValues.equals(vector3f)) {
            return; //Don't store anything.
        }
        JSONObject storedVector = new JSONObject();
        if (vector3f.x != defaultValues.x) {
            storedVector.put("x", vector3f.x);
        }
        if (vector3f.y != defaultValues.y) {
            storedVector.put("y", vector3f.y);
        }
        if (vector3f.z != defaultValues.z) {
            storedVector.put("z", vector3f.z);
        }
        jsonObject.put(name, storedVector);
    }

    public static void storeFloatOrNoneIfDefault(float value, JSONObject jsonObject, String name, float defaultValue) {
        if (value == defaultValue) {
            return;
        }
        jsonObject.put(name, value);
    }

    public static Vector3f parseVector3f(JSONObject jsonObject, String name) {
        JSONObject storedVector3f = (JSONObject) jsonObject.get(name);
        Vector3f vector3f = new Vector3f();
        vector3f.x = new Float((Double) storedVector3f.get("x"));
        vector3f.y = new Float((Double) storedVector3f.get("y"));
        vector3f.z = new Float((Double) storedVector3f.get("z"));
        return vector3f;
    }

    public static Vector3f parseVector3fOrUseDefaults(JSONObject jsonObject, String name, Vector3f defaultValues) {
        Object storedVector3fObject = jsonObject.get(name);
        Vector3f result = new Vector3f(defaultValues);
        if (storedVector3fObject == null) {
            return result;
        }
        if (storedVector3fObject instanceof JSONObject) {
            JSONObject storedVector3f = (JSONObject) storedVector3fObject;
            result.x = getFloatOrUseDefault(storedVector3f, "x", defaultValues.x);
            result.y = getFloatOrUseDefault(storedVector3f, "y", defaultValues.y);
            result.z = getFloatOrUseDefault(storedVector3f, "z", defaultValues.z);
        }
        return result;
    }

    public static float getFloatOrUseDefault(JSONObject jsonObject, String name, float defaultValue) {
        Object floatObject = jsonObject.get(name);
        if (floatObject == null) {
            return defaultValue;
        }
        if (floatObject instanceof Float) {
            return (float) floatObject;
        } else if (floatObject instanceof Double) {
            return new Float((Double) floatObject);
        }
        return defaultValue;
    }


}
