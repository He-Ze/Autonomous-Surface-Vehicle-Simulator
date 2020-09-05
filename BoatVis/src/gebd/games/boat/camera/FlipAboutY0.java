package gebd.games.boat.camera;

import composites.entities.EntityPositionHelper;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 9/15/2016.
 */
public class FlipAboutY0 {

    public static Quat4f flipRotationAboutPlaneYEquals0(Quat4f currentRotation) {

        //Determine the position (x, z) about which this rotation causes.
        Vector3f initialVector = new Vector3f(0, 0, -1);
        EntityPositionHelper.rotateAboutQuaternion(currentRotation, initialVector);

        //Determine the angle.
        double theta = Math.atan2(-initialVector.z, initialVector.x);
        if (Double.isNaN(theta) || Double.isInfinite(theta)) {
            //If x and z are both 0, it will give a bad angle.
            theta = 0f;
        }

        //Determine the Vector if it were flattened onto the plane y = 0.
        float flattenedAxisX = (float) Math.cos(theta);
        float flattenedAxisZ = (float) -Math.sin(theta);
        Vector3f flattenedAxis = new Vector3f(flattenedAxisX, 0, flattenedAxisZ);

        //Find the angle between the current rotation and the flattened angle.
        float dotProduct = initialVector.dot(flattenedAxis);
        float angleBetweenTwoVectors = dotProduct / (initialVector.length() * flattenedAxis.length());

        //Rotate the theta by 90 degrees and get the new vector as the cross product.
        float rotationAxisX = (float) Math.cos(theta + (Math.PI / 2.0));
        float rotationAxisZ = (float) -Math.sin(theta + (Math.PI / 2.0));

        //Create the flip quaterion rotation.
        AxisAngle4f rotationAxisAngle = new AxisAngle4f(rotationAxisX, 0, rotationAxisZ, angleBetweenTwoVectors * 2f);
        Quat4f flipRotation = new Quat4f();
        flipRotation.set(rotationAxisAngle);

        //Apply the rotation and return the result.
        Quat4f result = new Quat4f();
        result.set(currentRotation);
        result.mul(flipRotation);

        return result;
    }


}
