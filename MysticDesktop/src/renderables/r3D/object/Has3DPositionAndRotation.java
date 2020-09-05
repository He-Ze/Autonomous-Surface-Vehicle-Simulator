package renderables.r3D.object;

import composites.entities.EntityPositionHelper;
import gebd.Render;
import physics.util.Plane;
import renderables.r3D.rotation.Quat4fHelper;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 13/06/2016.
 */
public abstract class Has3DPositionAndRotation {

    protected Vector3f position = new Vector3f();
    private float theta = Float.NaN;
    private float phi = Float.NaN;
    private float roll = Float.NaN;
    protected boolean rotationRequiresUpdate = false;
    private Quat4f quatRotation = new Quat4f(0f, 1f, 0f, 0f);
    private Vector3f initialOrientationDirection = new Vector3f(0, 0, -1);

    public void setPosition(Vector3f newPosition) {
        setPosition(newPosition.x, newPosition.y, newPosition.z);
    }

    public void setPosition(float x, float y, float z){
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void increasePosition(Vector3f byVector){
        increasePosition(byVector.x, byVector.y, byVector.z);
    }

    public void increasePosition(float x, float y, float z){
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public Vector3f getPosition(){
        return position;
    }

    public void setRotation(Vector3f newRotation){
        setRotation(newRotation.x, newRotation.y, newRotation.z);
    }

    public void setRotation(float theta, float phi, float roll){
        setTheta(theta);
        setPhi(phi);
        setRoll(roll);
    }

    public void setRotation(float theta, float phi) {
        setPhi(phi);
        setTheta(theta);
    }

    public Vector3f getRotation(){
        return new Vector3f(theta, phi, roll);
    }

    public void increaseRotation(float theta, float phi, float roll) {
        incrementTheta(theta);
        incrementPhi(phi);
        incrementRoll(roll);
    }

    public void incrementTheta(float amount) {
        setTheta(theta + amount);
    }

    public void incrementPhi(float amount) {
        setPhi(phi + amount);
    }

    public void incrementRoll(float amount) {
        setRoll(roll + amount);
    }

    public float getTheta() {
        return theta;
    }

    public void setTheta(float theta) {
        this.rotationRequiresUpdate |= this.theta != theta;
        this.theta = theta;
    }

    public float getPhi() {
        return phi;
    }

    public void setPhi(float phi) {
        this.rotationRequiresUpdate |= this.phi != phi;
        this.phi = phi;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.rotationRequiresUpdate |= this.roll != roll;
        this.roll = roll;
    }

    public Quat4f getQuatRotation() {
        convertRotationToQuat4f(theta, phi, roll);
        return quatRotation;
    }

    private void convertRotationToQuat4f(float theta, float phi, float roll) {

        if (!rotationRequiresUpdate) {
            //Rotation is already up-to-date.
            return;
        }
        rotationRequiresUpdate = false;

        /*
        AxisAngle4f rollRotationAxis = new AxisAngle4f(initialOrientationDirection.x, initialOrientationDirection.y, initialOrientationDirection.z, roll);
        Quat4f rollRotation = new Quat4f();
        rollRotation.set(rollRotationAxis);

        Vector3f desiredNormal = getLookVector();
        AxisAngle4f lookAtAngles = Plane.rotatePlaneSoUpIsNowOtherThing(initialOrientationDirection, desiredNormal);
        //lookAtAngles.angle = getPhi();
        Quat4f lookAtRotation = new Quat4f();
        lookAtRotation.set(lookAtAngles);

        //TODO - Ensure that this is correct.
        //quatRotation.set(lookAtRotation);
        //quatRotation.mul(rollRotation);
        quatRotation.set(rollRotation);
        quatRotation.set(lookAtRotation);
//        quatRotation.set(rollRotation);
//        quatRotation.mul(lookAtRotation);


        Quat4fHelper.toQuat4f(-getPhi(), givenTheta, getRoll(), quatRotation);
        */

        //Convert.
        float givenTheta = (float) (getTheta() - (Math.PI / 2f));
        convertRotationToQuat4f(givenTheta, getPhi(), getRoll(), quatRotation);

        AxisAngle4f yAngle = new AxisAngle4f(0, 1, 0, givenTheta);
        Quat4f yRotation = new Quat4f();
        yRotation.set(yAngle);

        quatRotation.set(yRotation);

        AxisAngle4f xAngle = new AxisAngle4f(1, 0, 0, getPhi());
        Quat4f xRotation = new Quat4f();
        xRotation.set(xAngle);

        quatRotation.mul(xRotation);

        AxisAngle4f zAngle = new AxisAngle4f(0, 0, 1, getRoll()); //TODO - I have NO idea if this is correct...
        Quat4f zRotation = new Quat4f();
        zRotation.set(zAngle);

        quatRotation.mul(zRotation);



    }

    /**
     * Performs operations in the order: Roll, Phi, Theta.
     * @param theta
     * @param phi
     * @param roll
     * @param quat4f
     */
    public static void convertRotationToQuat4f(float theta, float phi, float roll, Quat4f quat4f) {

        AxisAngle4f yAngle = new AxisAngle4f(0, 1, 0, theta);
        Quat4f yRotation = new Quat4f();
        yRotation.set(yAngle);

        quat4f.set(yRotation);

        AxisAngle4f xAngle = new AxisAngle4f(1, 0, 0, phi);
        Quat4f xRotation = new Quat4f();
        xRotation.set(xAngle);

        quat4f.mul(xRotation);

        AxisAngle4f zAngle = new AxisAngle4f(0, 0, 1, roll); //TODO - I have NO idea if this is correct...
        Quat4f zRotation = new Quat4f();
        zRotation.set(zAngle);

        quat4f.mul(zRotation);
    }

    public Vector3f getLookVector() {
        float y = (float) Math.sin(phi);
        float r = (float) Math.cos(phi);
        float x = r * ((float) Math.cos(theta));
        float z = -r * ((float) Math.sin(theta));
        return new Vector3f(x, y, z);
    }

    /**
     * Determine the Euler angles from the current quaternion rotation.
     */
    public void determineEulerAngles() {
        Vector3f xyzRotation = new Vector3f();
        Quat4fHelper.toXYZRotation(getQuatRotation(), xyzRotation);
        setRotation(xyzRotation);
    }

}
