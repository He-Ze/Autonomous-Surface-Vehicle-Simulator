package physics.util;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 8/31/2016.
 */
public class OffsetHelper {

    public static Vector3f getOffset(RigidBody rigidBody, Vector3f offset) {
        Transform transform = new Transform();
        rigidBody.getMotionState().getWorldTransform(transform);
        return getOffset(transform, offset);
    }

    public static Vector3f getOffset(Transform transform, Vector3f offset) {
        return getOffset(transform.origin, transform.basis, offset);
    }

    public static Vector3f getOffset(Vector3f origin, Matrix3f rotationMatrix, Vector3f offset) {
        Vector3f result = new Vector3f(offset);
        rotationMatrix.transform(result);
        result.add(origin);
        return result;
    }

}
