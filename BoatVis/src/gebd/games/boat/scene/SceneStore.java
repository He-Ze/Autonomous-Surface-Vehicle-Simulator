package gebd.games.boat.scene;

import gebd.games.boat.json.util.Vector3fJson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.vecmath.Vector3f;

import java.util.ArrayList;

/**
 * Created by CaptainPete on 7/28/2016.
 */
public class SceneStore {

    public static void main(String[] args) {
        JSONObject obj = new JSONObject();

        obj.put("name", "Ysera");
        obj.put("num", new Integer(12));
        obj.put("balance", new Double(1000.21));
        obj.put("is_vip", new Boolean(true));

        JSONObject obj2 = new JSONObject();
        obj2.put("last_name", "Dragon");
        obj2.put("level", new Integer(150));

        obj.put("name_details", obj2);


        ArrayList<JSONObject> testList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            JSONObject newObj = new JSONObject();
            newObj.put("val1", "ASDF");
            newObj.put("val2", Math.random());
            testList.add(newObj);
        }

        obj.put("test_list", testList);

        Vector3f position = new Vector3f(1, 2, 3);
        Vector3fJson.storeVector3f(position, obj, "position");


        System.out.println(obj);
        JSONParser parser = new JSONParser();

        Object parsedObjObj = null;

        try {
            parsedObjObj = parser.parse(obj.toJSONString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject parsedObj = (JSONObject) parsedObjObj;

        Vector3f retreivedPosition = Vector3fJson.parseVector3f(obj, "position");

        System.out.println(parsedObj);



    }


}
