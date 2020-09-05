package util.settings;

import renderables.r3D.water.WaterSettings;

/**
 * Created by P3TE on 29/07/2016.
 * This may be modified, but as it is
 * a SettingsHolder with a parent.
 */
public class SettingsHolderWithParents<T extends Enum<? extends AttributeEnumInterface>> extends SettingsHolder<T>{

    private SettingsHolder<T> parentsCombinedSettings;
    private SettingsHolder<T> uniqueSettings;

    public SettingsHolderWithParents(T[] values) {
        super(values);
        parentsCombinedSettings = new SettingsHolder<>(values);
        uniqueSettings = new SettingsHolder<>(values);
    }

    @Override
    public void setValue(T givenEnum, Object newValue) {
        Object parentValue = parentsCombinedSettings.getValue(givenEnum);
        if (parentValue.equals(newValue)) {
            uniqueSettings.setValue(givenEnum, null);
        } else {
            uniqueSettings.setValue(givenEnum, newValue);
        }
        super.setValue(givenEnum, newValue);
    }

    public void setValuesOfParent(SettingsHolder<T> parentSettings) {
        for (int i = 0; i < parentSettings.currentValues.size(); i++) {
            Object parentValue = parentSettings.currentValues.get(i);
            if (parentValue != null) {
                setValueOfParent(values[i], parentValue);
            }
        }
    }

    public void setValueOfParent(T givenEnum, Object newValue) {
        parentsCombinedSettings.setValue(givenEnum, newValue);
        if (uniqueSettings.getValue(givenEnum) == null) {
            super.setValue(givenEnum, newValue);
        }
    }

    public void setValueOfParent(String attributeName, Object newValue) {
        T settingType = getSettingType(attributeName);
        if (settingType == null) {
            System.err.println("Unknown Attribute: " + attributeName + " for type WaterSettingType");
        } else {
            setValueOfParent(settingType, newValue);
        }
    }

    public SettingsHolder<T> getUniqueSettings() {
        return uniqueSettings;
    }

    public void setNewParent(SettingsHolder<T> newParent) {
        parentsCombinedSettings = newParent;
        for (T key : values) {
            if (uniqueSettings.getValueIncludingNonDefaults(key) == null) {
                super.setValue(key, newParent.getValue(key));
            }
        }
    }

    public SettingsHolderWithParents(T[] values, SettingsHolder<T> parentSettings) {
        super(values);
        setNewParent(parentSettings);
    }
}
