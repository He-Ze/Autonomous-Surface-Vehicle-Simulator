package gebd.games.boat.json.util;

import org.json.simple.JSONObject;
import util.settings.AttributeEnumInterface;
import util.settings.SettingsHolder;

import java.util.Map;

/**
 * Created by P3TE on 1/08/2016.
 */
public class SettingsHolderJson {

    public static void storeInformation(JSONObject jsonObject, String name, SettingsHolder settingsHolder) {
        JSONObject storedSettingsHolder = new JSONObject();
        Enum<? extends AttributeEnumInterface>[] valueTypes = settingsHolder.getValues();
        for (Enum<? extends AttributeEnumInterface> enumType : valueTypes) {
            Object value = settingsHolder.getValueIncludingNonDefaults(enumType);
            if (value != null) {
                String attributeName = ((AttributeEnumInterface) enumType).getAttributeName();
                storedSettingsHolder.put(attributeName, value);
            }
        }
        jsonObject.put(name, storedSettingsHolder);
    }

    public static void loadSettingsHolder(JSONObject settingsHolderJson, SettingsHolder settingsHolder) {
        for (Object entryObject : settingsHolderJson.entrySet()) {
            Map.Entry entry = (Map.Entry) entryObject;
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            settingsHolder.setValue(key, value);
        }
    }

}
