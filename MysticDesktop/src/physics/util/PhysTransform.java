package physics.util;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 9/3/2016.
 */
public class PhysTransform {

    public static Vector3f getGlPosition(RigidBody rigidBody) {
        Transform transform = new Transform();
        rigidBody.getMotionState().getWorldTransform(transform);
        return toGlPosition(transform.origin);
    }

    public static Vector3f toPhysPosition(Vector3f glPosition) {
        return new Vector3f(glPosition.z, glPosition.y, glPosition.x);
    }

    public static Vector3f toGlPosition(Vector3f physicsPosition) {
        return toPhysPosition(physicsPosition);
    }

    public static Quat4f getGlRotation(RigidBody rigidBody) {
        Transform transform = new Transform();
        rigidBody.getMotionState().getWorldTransform(transform);
        Quat4f physicsQuat4f = new Quat4f();
        physicsQuat4f.set(transform.basis);
        return toGlRotation(physicsQuat4f);
    }

    public static AxisAngle4f toPhysAngle(AxisAngle4f glRotation) {
        return new AxisAngle4f(glRotation.z, glRotation.y, glRotation.x, glRotation.angle);
    }

    public static AxisAngle4f toGlAngle(AxisAngle4f physicsRotation) {
        return toPhysAngle(physicsRotation);
    }

    public static Quat4f toPhysRotation(Quat4f glRotation) {
        return new Quat4f(glRotation.z, glRotation.y, glRotation.x, glRotation.w);
    }

    public static Quat4f toGlRotation(Matrix3f basis) {
        Quat4f physicsRotation = new Quat4f();
        physicsRotation.set(basis);
        return toGlRotation(physicsRotation);
    }

    public static Quat4f toGlRotation(Quat4f physicsRotation) {
        return toPhysRotation(physicsRotation);
    }



}
