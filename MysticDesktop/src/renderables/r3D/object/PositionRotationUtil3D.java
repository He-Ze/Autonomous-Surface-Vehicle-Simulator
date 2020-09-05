package renderables.r3D.object;

import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 28/06/2016.
 */
public class PositionRotationUtil3D {

    public static void setXRotation(Has3DPositionAndRotation object, float newValue) {
        Vector3f existingRotation = object.getRotation();
        existingRotation.x = newValue;
    }

    public static void setYRotation(Has3DPositionAndRotation object, float newValue) {
        Vector3f existingRotation = object.getRotation();
        existingRotation.y = newValue;
    }

    public static void setZRotation(Has3DPositionAndRotation object, float newValue) {
        Vector3f existingRotation = object.getRotation();
        existingRotation.z = newValue;
    }

    public static void setYPosition(Has3DPositionAndRotation object, float newValue) {
        Vector3f existingPosition = object.getPosition();
        existingPosition.y = newValue;
    }

}
