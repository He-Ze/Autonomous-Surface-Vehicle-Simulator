package util.settings;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CaptainPete on 7/29/2016.
 * This is some crazy enum stuff, I think I've gone insane, I'm hearing voices.
 * (Children of the Sun)
 */
public class SettingsHolder <T extends Enum<? extends AttributeEnumInterface>>{

    protected HashMap<String, Integer> attributeMap;
    protected ArrayList<Object> currentValues;
    protected T[] values;

    public SettingsHolder(T[] values) {
        this.values = values;
        currentValues = new ArrayList<>(values.length);
        attributeMap = new HashMap<>(values.length);
        for (int i = 0; i < values.length; i++) {
            AttributeEnumInterface attributeEnumInterface = (AttributeEnumInterface) values[i];
            currentValues.add(null);
            attributeMap.put(attributeEnumInterface.getAttributeName(), i);
        }
    }

    public void setValue(T givenEnum, Object newValue) {
        if ((newValue != null) && !((AttributeEnumInterface) givenEnum).getAttributeClassType().isAssignableFrom(newValue.getClass())) {
            if ((((AttributeEnumInterface) givenEnum).getAttributeClassType() == Float.class) && (newValue.getClass() == Double.class)) {
                newValue = new Float(((Double) newValue).doubleValue());
            } else {
                throw new RuntimeException("Incompatible Variable Types!");
            }
        }
        Object defaultValue = ((AttributeEnumInterface) givenEnum).getDefaultValue();
        if (defaultValue.equals(newValue)) {
            //It's back to using defaults.
            currentValues.set(givenEnum.ordinal(), null);
        } else {
            currentValues.set(givenEnum.ordinal(), newValue);
        }
    }

    public void setValue(String attributeName, Object newValue) {
        T settingType = getSettingType(attributeName);
        if (settingType == null) {
            System.err.println("Unknown Attribute: " + attributeName + " for type WaterSettingType");
        } else {
            setValue(settingType, newValue);
        }
    }

    public T getSettingType(String attributeName) {
        for (T settingType : values) {
            if (((AttributeEnumInterface) settingType).getAttributeName().equals(attributeName)) {
                return settingType;
            }
        }
        return null;
    }

    public Object getValue(T givenEnum) {
        return getValueUsingDefaults(givenEnum.ordinal());
    }

    public Object getDefaultValue(T givenEnum) {
        return ((AttributeEnumInterface) values[givenEnum.ordinal()]).getDefaultValue();
    }

    public Object getValue(String attributeName) {
        Integer ordinalIndex = attributeMap.get(attributeName);
        if (ordinalIndex == null) {
            System.err.println("Value with attribute name: " + attributeName + " does not exist!");
            return null;
        }
        return getValueUsingDefaults(ordinalIndex);
    }

    protected Object getValueUsingDefaults(int index) {
        Object value = currentValues.get(index);
        if (value == null) {
            value = ((AttributeEnumInterface) values[index]).getDefaultValue();
        }
        return value;
    }

    public void setValueToDefault(T givenEnum) {
        currentValues.set(givenEnum.ordinal(), null);
    }

    public void setValueToDefault(String attributeName) {
        Integer ordinalIndex = attributeMap.get(attributeName);
        if (ordinalIndex == null) {
            System.err.println("Value with attribute name: " + attributeName + " does not exist!");
        } else {
            currentValues.set(ordinalIndex, null);
        }
    }

    public void setAllValuesToDefault(){
        for (int i = 0; i < currentValues.size(); i++) {
            currentValues.set(i, null);
        }
    }

    public Object getValueIncludingNonDefaults(T givenEnum) {
        return currentValues.get(givenEnum.ordinal());
    }

    public T[] getValues() {
        return values;
    }
}
