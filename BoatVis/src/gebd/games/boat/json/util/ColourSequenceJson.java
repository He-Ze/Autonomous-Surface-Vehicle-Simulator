package gebd.games.boat.json.util;

import gebd.games.boat.BoatVis;
import gebd.games.boat.entity.ColourSequenceNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.vecmath.Vector3f;
import java.util.ArrayList;

/**
 * Created by CaptainPete on 2016-11-15.
 */
public class ColourSequenceJson {

    public static final String COL_SEQ_NAME = "colour_sequence";
    private static final String RED_PARAM_NAME = "red";
    private static final String GREEN_PARAM_NAME = "green";
    private static final String BLUE_PARAM_NAME = "blue";
    private static final String BLEND_PARAM_NAME = "blend";
    private static final String DURATION_PARAM_NAME = "duration";

    public static void storeJSON(JSONObject jsonObject, BoatVis boatVis) {

        JSONArray jsonArray = new JSONArray();

        for (ColourSequenceNode colourSequenceNode : boatVis.getColourSequenceHandler().getColourSequenceNodes()) {
            jsonArray.add(storeColourNode(colourSequenceNode));
        }

        jsonObject.put(COL_SEQ_NAME, jsonArray);

    }

    private static JSONObject storeColourNode(ColourSequenceNode colourSequenceNode) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RED_PARAM_NAME, colourSequenceNode.getColour().x);
        jsonObject.put(GREEN_PARAM_NAME, colourSequenceNode.getColour().y);
        jsonObject.put(BLUE_PARAM_NAME, colourSequenceNode.getColour().z);

        jsonObject.put(BLEND_PARAM_NAME, colourSequenceNode.getColourBlendAmount());
        jsonObject.put(DURATION_PARAM_NAME, colourSequenceNode.getDuration());

        return jsonObject;
    }

    public static void loadJSON(JSONObject jsonObject, BoatVis boatVis) {
        ArrayList<ColourSequenceNode> colourSequenceNodes = boatVis.getColourSequenceHandler().getColourSequenceNodes();

        Object storedObject = jsonObject.get(COL_SEQ_NAME);
        if (storedObject == null) {
            //Didn't exist.
            return;
        }

        if (!(storedObject instanceof JSONArray)) {
            //Not a valid datatype.
            return;
        }

        colourSequenceNodes.clear();
        boatVis.getColourSequenceDropdown().resetAllTimings();

        JSONArray jsonArray = (JSONArray) storedObject;
        for (int objectNo = 0; objectNo < jsonArray.size(); objectNo++) {
            Object storedColourNodeObject = jsonArray.get(objectNo);
            if (storedColourNodeObject instanceof JSONObject) {
                ColourSequenceNode colourSequenceNode = tryLoadColourSequenceNode((JSONObject) storedColourNodeObject);
                if (colourSequenceNode != null) {
                    boatVis.getColourSequenceDropdown().addColourSequence(colourSequenceNode);
                }
            }
        }


    }

    private static ColourSequenceNode tryLoadColourSequenceNode(JSONObject jsonObject) {
        try {
            Vector3f colour = new Vector3f();
            colour.x = new Float((Double) jsonObject.get(RED_PARAM_NAME));
            colour.y = new Float((Double) jsonObject.get(GREEN_PARAM_NAME));
            colour.z = new Float((Double) jsonObject.get(BLUE_PARAM_NAME));

            float colourBlend = new Float((Double) jsonObject.get(BLEND_PARAM_NAME));
            float duration = new Float((Double) jsonObject.get(DURATION_PARAM_NAME));

            ColourSequenceNode colourSequenceNode = new ColourSequenceNode(colour, colourBlend, duration);

            return colourSequenceNode;
        } catch (Exception e) {
            System.err.println("Unable to load colour sequence node: " + String.valueOf(jsonObject));
        }

        return null;
    }


}
