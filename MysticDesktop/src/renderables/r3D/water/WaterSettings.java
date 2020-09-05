package renderables.r3D.water;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import util.settings.AttributeEnumInterface;

/**
 * Created by P3TE on 13/06/2016.
 */
public enum WaterSettings implements AttributeEnumInterface {
    MURKINESS("murkiness", Float.class, 0.1f),
    RED_COLOUR("red_colour", Float.class, 0.4f),
    GREEN_COLOUR("green_colour", Float.class, 0.4f),
    BLUE_COLOUR("blue_colour", Float.class, 1f),
    WAVE_STRENGTH("wave_strength", Float.class, 0.025f),
    WATER_REFLECTIVITY("water_reflectivity", Float.class, 3.0f),
    TILING_X("tiling_x", Float.class, 24f),
    TILING_Y("tiling_y", Float.class, 24f),
    WATER_ROTATION("water_rotation", Float.class, 0.0f);

    private String attributeName;
    private Class<?> attributeClassType;
    private Object defaultValue;
    WaterSettings(String attributeName, Class<?> attributeClassType, Object defaultValue) {
        this.attributeName = attributeName;
        this.attributeClassType = attributeClassType;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getAttributeName() {
        return attributeName;
    }

    @Override
    public Class<?> getAttributeClassType() {
        return attributeClassType;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    public static final Vector3f DEFAULT_WATER_COLOUR = new Vector3f((float) RED_COLOUR.getDefaultValue(),
            (float) GREEN_COLOUR.getDefaultValue(),
            (float) BLUE_COLOUR.getDefaultValue());

    public static final Vector2f DEFAULT_TILING = new Vector2f((float) TILING_X.getDefaultValue(),
            (float) TILING_Y.getDefaultValue());

}
