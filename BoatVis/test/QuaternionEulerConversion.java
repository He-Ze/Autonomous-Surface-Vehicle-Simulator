import org.junit.Test;
import renderables.r3D.object.Has3DPositionAndRotation;
import renderables.r3D.rotation.Quat4fHelper;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by p3te on 27/10/16.
 */
public class QuaternionEulerConversion {

    @Test
    public void TestConversion() {

        int NUM_TRIES = 100;

        for (int i = 0; i < NUM_TRIES; i++) {

            double theta = Math.random() * Math.PI * 2.0;
            double phi = Math.random() * Math.PI * 2.0;
            double z = Math.random() * Math.PI * 2.0;

            Vector3f euler = new Vector3f((float) theta, (float) phi, (float) z);

            Quat4f quat4fRotation = new Quat4f();

            //Populate the quaternion.
            Has3DPositionAndRotation.convertRotationToQuat4f((float) theta, (float) phi, (float) z, quat4fRotation);


            Vector3f convertEuler2 = new Vector3f();
            Quat4fHelper.toXYZRotation(quat4fRotation, convertEuler2);

            Quat4f convertQuat2 = new Quat4f();
            Quat4fHelper.toQuat4f(euler, convertQuat2);

            assertEulerEquivalence(euler, convertEuler2);

            assertQuaternionEquivalence(quat4fRotation, convertQuat2);

        }

    }

    public boolean assertQuaternionEquivalence(Quat4f expected, Quat4f trueValue) {

        if (!sameFloatingPointWithTolerance(expected.x, trueValue.x)) {
            return false;
        }

        if (!sameFloatingPointWithTolerance(expected.y, trueValue.y)) {
            return false;
        }

        if (!sameFloatingPointWithTolerance(expected.z, trueValue.z)) {
            return false;
        }

        if (!sameFloatingPointWithTolerance(expected.w, trueValue.w)) {
            return false;
        }

        return true;

    }


    public boolean assertEulerEquivalence(Vector3f expected, Vector3f trueValue) {

        final double tolerance = 0.000001;

        if (!sameFloatingPointWithTolerance(expected.x, trueValue.x)) {
            return false;
        }

        if (!sameFloatingPointWithTolerance(expected.y, trueValue.y)) {
            return false;
        }

        if (!sameFloatingPointWithTolerance(expected.z, trueValue.z)) {
            return false;
        }

        return true;

    }

    public boolean sameFloatingPointWithTolerance(float value1, float value2) {

        final float tolerance = 0.000001f;

        if (Math.abs(value1 - value2) > tolerance) {

            System.out.println("value1 != value2 (" + value1 + " != " + value2 + ")");

            return false;
        }

        return true;

    }


    @Test
    public void dodgyThingTest() {
        boolean success = false;
        while (!success) {
            success = dodgyThingTestHelper();
        }
    }


    public boolean dodgyThingTestHelper(){


        double theta = Math.random() * Math.PI * 2.0;
        double phi = Math.random() * Math.PI * 2.0;
        double z = Math.random() * Math.PI * 2.0;

        Vector3f euler = new Vector3f((float) theta, (float) phi, (float) z);

        Quat4f quat4fRotation = new Quat4f();

        //Populate the quaternion.
        Has3DPositionAndRotation.convertRotationToQuat4f((float) theta, (float) phi, (float) z, quat4fRotation);


        float[] quatVals = new float[4];
        quat4fRotation.get(quatVals);
        int[] indexQuat = {0, 1, 2, 3};
        shuffle(indexQuat);
        Quat4f shuffledQuat = new Quat4f(fromIndicies(quatVals, indexQuat));

        Vector3f calculatedEuler = new Vector3f();
        Quat4fHelper.toXYZRotation(shuffledQuat, calculatedEuler);
        boolean equivalent = assertEulerEquivalence(euler, calculatedEuler);
        if (equivalent) {
            System.out.println(indexQuat);
            System.out.println(Arrays.toString(indexQuat));
        } else {
            return false;
        }


        float[] eulerVals = new float[3];
        euler.get(eulerVals);
        int[] indexEuler = {0, 1, 2};
        shuffle(indexEuler);
        Vector3f shuffledEuler = new Vector3f(fromIndicies(eulerVals, indexEuler));

        Quat4f calculatedQuat = new Quat4f();
        Quat4fHelper.toQuat4f(new Vector3f(fromIndicies(eulerVals, indexEuler)), calculatedQuat);
        equivalent &= assertQuaternionEquivalence(quat4fRotation, calculatedQuat);

        if (equivalent) {
            System.out.println(indexEuler);
            System.out.println(Arrays.toString(indexEuler));
        }

        return equivalent;
    }

    private float[] fromIndicies(float[] values, int[] indicies) {
        float[] result = new float[values.length];

        for (int i = 0; i < values.length; i++) {
            result[i] = values[indicies[i]];
        }

        return result;
    }


    private static void shuffle(int[] values) {


        int arrayLength = values.length;

        for (int i = 0; i < arrayLength; i++) {

            int currentMax = arrayLength - (i + 1);
            int randomIndex = (int) (Math.random() * (arrayLength - i));

            int tempVal = values[currentMax];
            values[currentMax] = values[randomIndex];
            values[randomIndex] = tempVal;

        }

    }

















}
