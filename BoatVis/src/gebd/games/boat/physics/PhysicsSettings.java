package gebd.games.boat.physics;

import util.settings.AttributeEnumInterface;

/**
 * Created by CaptainPete on 9/22/2016.
 */
public enum PhysicsSettings implements AttributeEnumInterface {
    USE_PHYSICS_BOAT("use_physics_boat", Boolean.class, true),

    BOAT_DEFAULT_POSITION_X("physics_boat_default_position_x", Float.class, 0.0f),
    BOAT_DEFAULT_POSITION_Y("physics_boat_default_position_y", Float.class, 0.0f),
    BOAT_DEFAULT_POSITION_Z("physics_boat_default_position_z", Float.class, 0.0f),

    BOAT_DEFAULT_ROTATION_X("physics_boat_default_rotation_x", Float.class, 0.0f),
    BOAT_DEFAULT_ROTATION_Y("physics_boat_default_rotation_y", Float.class, 0.99985236f),
    BOAT_DEFAULT_ROTATION_Z("physics_boat_default_rotation_z", Float.class, 0.0f),
    BOAT_DEFAULT_ROTATION_W("physics_boat_default_rotation_w", Float.class, 0.0171f),

    PHYSICS_SPEED_MODIFIER("physics_speed_modifier", Float.class, 1.0f),
    USE_SIMPLE_WATER("use_simple_water", Boolean.class, true),
    WAVE_HEIGHT("wave_height", Float.class, 0.0f),
    WATER_DIRECTION("water_direction", Float.class, 0.0f),
    WATER_FREQUENCY("water_frequency", Float.class, 5.0f),
    DISTANCE_BEWTEEN_WAVES("distance_between_waves", Float.class, 5.0f);

    private String attributeName;
    private Class<?> attributeClassType;
    private Object defaultValue;
    PhysicsSettings(String attributeName, Class<?> attributeClassType, Object defaultValue) {
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
}
