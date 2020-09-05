package renderables.r3D.rotation;

import javax.vecmath.*;

/**
 * Created by CaptainPete on 9/6/2016.
 */
public class Quat4fHelper {

    public static void toQuat4f(Vector3f xyzRotation, Quat4f quat4f) {
        toQuat4f(xyzRotation.x, xyzRotation.y, xyzRotation.z, quat4f);
    }

    public static void toQuat4f(float psi, float theata, float phi, Quat4f quat4f) {
        float phiOnTwo = phi / 2f;
        float cosOfPhiOnTwo = (float) Math.cos(phiOnTwo);
        float sinOfPhiOnTwo = (float) Math.sin(phiOnTwo);

        float thetaOnTwo = theata / 2f;
        float cosOfThetaOnTwo = (float) Math.cos(thetaOnTwo);
        float sinOfThetaOnTwo = (float) Math.sin(thetaOnTwo);

        float psiOnTwo = psi / 2f;
        float cosOfPsiOnTwo = (float) Math.cos(psiOnTwo);
        float sinOfPsiOnTwo = (float) Math.sin(psiOnTwo);

//        quat4f.x = (cosOfPhiOnTwo * cosOfThetaOnTwo * cosOfPsiOnTwo) + (sinOfPhiOnTwo * sinOfThetaOnTwo * sinOfPsiOnTwo);
//        quat4f.y = (sinOfPhiOnTwo * cosOfThetaOnTwo * cosOfPsiOnTwo) + (cosOfPhiOnTwo * sinOfThetaOnTwo * sinOfPsiOnTwo);
//        quat4f.z = (cosOfPhiOnTwo * sinOfThetaOnTwo * cosOfPsiOnTwo) + (sinOfPhiOnTwo * cosOfThetaOnTwo * sinOfPsiOnTwo);
//        quat4f.w = (cosOfPhiOnTwo * cosOfThetaOnTwo * sinOfPsiOnTwo) + (sinOfPhiOnTwo * sinOfThetaOnTwo * cosOfPsiOnTwo);

        quat4f.x = (cosOfPhiOnTwo * cosOfThetaOnTwo * sinOfPsiOnTwo) + (sinOfPhiOnTwo * sinOfThetaOnTwo * cosOfPsiOnTwo);
        quat4f.y = (cosOfPhiOnTwo * sinOfThetaOnTwo * cosOfPsiOnTwo) + (sinOfPhiOnTwo * cosOfThetaOnTwo * sinOfPsiOnTwo);
        quat4f.z = -((sinOfPhiOnTwo * cosOfThetaOnTwo * cosOfPsiOnTwo) + (cosOfPhiOnTwo * sinOfThetaOnTwo * sinOfPsiOnTwo));
        quat4f.w = (cosOfPhiOnTwo * cosOfThetaOnTwo * cosOfPsiOnTwo) + (sinOfPhiOnTwo * sinOfThetaOnTwo * sinOfPsiOnTwo);

        //quat4f.set(0,0,0,1);
    }

    public static void toXYZRotation(Quat4f quat4f, Vector3f xyzRotation) {
        float q0 = quat4f.w;
        float q2 = quat4f.x;
        float q1 = quat4f.y;
        float q3 = quat4f.z;

        xyzRotation.x = (float) Math.atan2((2 * (q0 * q1 + q2 * q3)) , (1 - (2 * (q1 * q1 + q2 * q2))));
        xyzRotation.y = (float) Math.asin(2 * (q0 * q2 - q3 * q1));
        xyzRotation.z = (float) Math.atan2((2 * (q0 * q3 + q1 * q2)) , (1 - (2 * (q2 * q2 + q3 * q3))));
    }


    @Deprecated
    public static void rotateAboutQuaternion(Quat4f quat4f, Vector3f position) {
        quat4f.normalize();
        Quat4f result = new Quat4f(quat4f);
        Quat4f temp = new Quat4f(position.x, position.y, position.z, 0f);
        result.mul(temp);
        temp.set(-quat4f.x, -quat4f.y, -quat4f.z, quat4f.w);
        result.mul(temp);
        float multiplier = position.length();
        position.x = result.x * multiplier;
        position.y = result.y * multiplier;
        position.z = result.z * multiplier;
    }


    public static void flipAboutXAxis(Quat4f quat4f) {
        quat4f.x *= -1;
        quat4f.w *= -1;
    }

    public static void flipAboutYAxis(Quat4f quat4f) {
        quat4f.y *= -1;
        quat4f.w *= -1;
    }

    public static void flipAboutZAxis(Quat4f quat4f) {
        quat4f.z *= -1;
        quat4f.w *= -1;
    }

}
