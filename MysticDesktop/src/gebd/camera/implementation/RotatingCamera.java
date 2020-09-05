package gebd.camera.implementation;

import blindmystics.input.CurrentInput;
import blindmystics.input.KeyboardInputLatchGenerator;
import blindmystics.util.input.InputLatch;
import blindmystics.util.input.mouse.ButtonStatus;
import blindmystics.util.input.mouse.InputStatus;
import gebd.camera.Camera;
import org.lwjgl.input.Keyboard;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import renderables.r3D.object.Has3DPositionAndRotation;

/**
 * Created by p3te on 26/08/16.
 */
public class RotatingCamera extends Camera {

    private Vector3f rotationPoint = new Vector3f();

    private boolean currentlySpinning = true;

    private float spinRate;
    private float distance;
    private float lookTheata = 0f;
    //private float lookPhi = (float) (Math.PI / 2.0);
    //private float lookPhi = 0.0f;
    private float lookPhi = 0f;

    public static final float DISTANCE_RATE_OF_CHANGE = 5.0f / 1000.0f;

    public static final float MAXIMUM_SPIN_RATE = (float) ((Math.PI * 2.0 * 3) / 1000.0);
    public static final float MINIMUM_SPIN_RATE = (float) ((Math.PI * 0.1) / 1000.0);

    private static InputLatch decreaseDistanceHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_W);
    private static InputLatch increaseDistanceHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_S);

    private static InputLatch startStopSpinHandler = KeyboardInputLatchGenerator.generateInputLatchForKey(Keyboard.KEY_P);

    public static final float MOUSE_ROTATION_RATE_OF_CHANGE = 0.005f;

    Vector2f previousMousePosition = null;

    public RotatingCamera() {
        this.spinRate = MINIMUM_SPIN_RATE;
        this.distance = 5;
        lookTheata = (float) (Math.PI * (3.0 / 2.0));
    }

    @Override
    protected void updateCamera(float delta, CurrentInput input) {


        //Increase / Decrease the distance to the object.
        float distanceIncreaseAmount = 0f;
        if (increaseDistanceHandler.isHeld()) {
            distanceIncreaseAmount += DISTANCE_RATE_OF_CHANGE * delta;
        }
        if (decreaseDistanceHandler.isHeld()) {
            distanceIncreaseAmount -= DISTANCE_RATE_OF_CHANGE * delta;
        }
        distance += distanceIncreaseAmount;

        //Limit the distance to within the bounds.
        distance = limit(distance, getMinimumDistance(), getMaximumDistance());
        spinRate = limit(spinRate, MINIMUM_SPIN_RATE, MAXIMUM_SPIN_RATE);


        //Toggle auto spinning.
        if (startStopSpinHandler.justPressed()) {
            currentlySpinning = !currentlySpinning;
        }


        //Increase the things.
        if (currentlySpinning) {
            lookTheata += (spinRate * delta);
        }


        if (InputStatus.isButtonDown(input.getLeftMouse())) {

        }
        if (input.getLeftMouse() == ButtonStatus.JUST_PRESSED) {
            previousMousePosition = input.getViewPortSpaceOfMouse();
        } else if (input.getLeftMouse() == ButtonStatus.DOWN) {
            Vector2f newPosition = input.getViewPortSpaceOfMouse();

            if (previousMousePosition != null) {
                float changeInX = previousMousePosition.x - newPosition.x;
                lookTheata += changeInX * MOUSE_ROTATION_RATE_OF_CHANGE;

                float changeInY = previousMousePosition.y - newPosition.y;
                lookPhi -= changeInY * MOUSE_ROTATION_RATE_OF_CHANGE;

            }

            previousMousePosition = newPosition;
        } else {
            previousMousePosition = null;
        }



        //Update the camera's position and rotation.
        updatePositionAndRotation();
    }



    private void updatePositionAndRotation() {

        if (lookPhi > (Math.PI / 2.0)) {
            lookPhi = (float) (Math.PI / 2.0);
        } else if (lookPhi < (-Math.PI / 2.0)) {
            lookPhi = (float) (-Math.PI / 2.0);
        }

        double yOffset = -distance * Math.sin(lookPhi);
        double rOffset = distance * Math.cos(lookPhi);
        double xOffset = rOffset * Math.cos(lookTheata);
        double zOffset = -rOffset * Math.sin(lookTheata);

        float cameraPositionX = (float) (rotationPoint.x + xOffset);
        float cameraPositionY = (float) (rotationPoint.y + yOffset);
        float cameraPositionZ = (float) (rotationPoint.z + zOffset);

        setPosition(cameraPositionX, cameraPositionY, cameraPositionZ);
        setPhi((float) (lookPhi));
        setTheta((float) (Math.PI + lookTheata));
    }



    private float limit(float value, float min, float max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }

    public float getMinimumDistance() {
        return getProjectionMatrixHandler().getNearPlane();
    }

    public float getMaximumDistance() {
        return getProjectionMatrixHandler().getFarPlane();
    }

    @Override
    public void resetCamera() {

    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setRotationPoint(Has3DPositionAndRotation has3DPositionAndRotation) {
        setRotation(has3DPositionAndRotation.getPosition());
    }

    public void setRotationPoint(Vector3f vector3f) {
        setRotation(vector3f.x, vector3f.y, vector3f.z);
    }

    public void setRotationPoint(float x, float y, float z) {
        rotationPoint.x = x;
        rotationPoint.y = y;
        rotationPoint.z = z;
    }

    public void setLookPhi(float lookPhi) {
        this.lookPhi = lookPhi;
    }
}
