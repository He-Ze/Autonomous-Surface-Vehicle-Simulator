package composites.entities;

import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 19/07/2016.
 */
public class RotationPosition {
    public Vector3f position;
    public Vector3f rotation;

    public RotationPosition(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}