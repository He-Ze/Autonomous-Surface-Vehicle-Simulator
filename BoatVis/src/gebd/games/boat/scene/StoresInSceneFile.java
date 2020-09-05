package gebd.games.boat.scene;

import org.json.simple.JSONObject;

/**
 * Created by CaptainPete on 7/28/2016.
 */
public interface StoresInSceneFile {

    void storeInformation(JSONObject object);
    void loadInformation(JSONObject object);

}
