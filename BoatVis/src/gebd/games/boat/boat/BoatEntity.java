package gebd.games.boat.boat;

import blindmystics.input.CurrentInput;
import blindmystics.util.input.keyboard.HandleKeyboard;
import composites.entities.Entity;
import composites.entities.EntityPositionHelper;
import gebd.games.boat.BoatVis;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import renderables.r3D.model.ModelsetModel;
import renderables.r3D.object.PositionRotationUtil3D;

/**
 * Created by CaptainPete on 28/06/2016.
 */
public class BoatEntity extends Entity {

    private BoatVis boatVis;
    public static final float BOAT_ROTATION_OFFSET = (float) (Math.PI / 2.0);

    public static float boatSpeed = 3f / 1000f;
    private float rotationSpeed = (float) (Math.PI / (1000f * 2));
    private Vector3f goalBoatPosition = new Vector3f();

    public static final float distanceAboveWater = 0.72f;

    //private float rollVelocity = 10 / 1000f;
    //private float pitchVelocity = 20 / 1000f;

    private float rollVelocity = 0 / 1000f;
    private float pitchVelocity = 0 / 1000f;

    public BoatEntity(String name, ModelsetModel model, String texturePath, Vector3f position, Vector3f size, Vector3f rotation, BoatVis boatVis) {
        super(name, model, texturePath, position, size, rotation);
        this.boatVis = boatVis;
        setupCorners();
    }

    Vector3f offsetPosition1;
    Vector3f offsetPosition2;
    Vector3f offsetPosition3;
    Vector3f offsetPosition4;

    Vector3f currentVelocity = new Vector3f();


    private void setupCorners() {
        Vector3f modelSize = model.getSize();
        Vector3f adjustedSizeMultiplier = size;
        Vector3f totalSize = new Vector3f(modelSize);
        totalSize.x *= adjustedSizeMultiplier.x;
        totalSize.y *= adjustedSizeMultiplier.y;
        totalSize.z *= adjustedSizeMultiplier.z;


        offsetPosition1 = new Vector3f(totalSize.x / 2f, 0, totalSize.z / 2f);
        offsetPosition2 = new Vector3f(totalSize.x / 2f, 0, -totalSize.z / 2f);
        offsetPosition3 = new Vector3f(-totalSize.x / 2f, 0, totalSize.z / 2f);
        offsetPosition4 = new Vector3f(-totalSize.x / 2f, 0, -totalSize.z / 2f);
    }

    public void setGoalBoatPosition(Vector3f goalPosition) {
        setGoalBoatPosition(goalPosition.x, goalPosition.y, goalPosition.z);
    }

    public void setGoalBoatPosition(float goalX, float goalY, float goalZ) {
        goalBoatPosition.x = goalX;
        goalBoatPosition.y = goalY;
        goalBoatPosition.z = goalZ;
    }

    @Override
    public void update(CurrentInput input, float delta) {
        super.update(input, delta);

        float currentPitch = getRotation().x;
        float currentRoll = getRotation().z;

        pitchVelocity -= (currentPitch * (delta / 1000f));
        rollVelocity -= (currentRoll * (delta / 1000f));

        //incrementTheta(-0.00001f * delta);

//        increaseRotX(pitchVelocity * (delta / 1000f));
//        increaseRotZ(rollVelocity * (delta / 1000f));

        float distTraveled = boatSpeed * delta;
        float distRotated = rotationSpeed * delta;
        float xDistToGoal = goalBoatPosition.x - getPosition().x;
        float zDistToGoal = goalBoatPosition.z - getPosition().z;
        float distToGoal = (float) (Math.sqrt(Math.pow(xDistToGoal, 2) + Math.pow(zDistToGoal, 2)));

        double currentTheta = Math.atan2(-zDistToGoal, xDistToGoal);
        if (distToGoal < distTraveled) {
            setPosition(goalBoatPosition.x, distanceAboveWater, goalBoatPosition.z);
            currentVelocity.set(0, 0, 0); //0 Velocity.
        } else {
            double xMove = distTraveled * Math.cos(currentTheta);
            double zMove = - distTraveled * Math.sin(currentTheta);
            float newXPos = (float) (getPosition().x + xMove);
            float newZPos = (float) (getPosition().z + zMove);
            currentVelocity.x = (float) (boatSpeed * 1000 * Math.cos(currentTheta)); //In meters/second.
            currentVelocity.y = 0;
            currentVelocity.z = (float) (boatSpeed * 1000 * Math.sin(currentTheta)); //In meters/second.
            setPosition(newXPos, distanceAboveWater, newZPos);


            setRotation((float) currentTheta, 0f, 0f);
            //setRotation((float) currentTheta, (float) (-Math.PI / 4.0), 0f);
//
//            double direction = 1;
//            double currentRotation = (currentYRotation - BOAT_ROTATION_OFFSET);
//            if (currentRotation > Math.PI) {
//                currentRotation = (-Math.PI) + (currentRotation - Math.PI);
//            } else if (currentRotation < -Math.PI) {
//                currentRotation = (Math.PI) + (currentRotation + Math.PI);
//            }
//            double diffRotataions = currentRotation - currentTheta;
//            if (diffRotataions < 0) {
//                direction = 1;
//            } else {
//                direction = -1;
//            }
//            if(Math.abs(diffRotataions) > Math.PI){
//                direction *= -1;
//            }
//            if (Math.abs(currentRotation - currentTheta) < distRotated) {
//                PositionRotationUtil3D.setYRotation(this, (float) (currentTheta + BOAT_ROTATION_OFFSET));
//            } else {
//                currentYRotation = (float) (currentRotation + (distRotated * direction));
//                if (Math.abs(currentYRotation - currentTheta) < distRotated) {
//                    //PositionRotationUtil3D.setYRotation(this, (float) (currentTheta + BOAT_ROTATION_OFFSET));
//                } else {
//                    //PositionRotationUtil3D.setYRotation(this, (float) (newRotation + BOAT_ROTATION_OFFSET));
//
//                }
//            }
        }

        //determineWaterHeightOffsets();

    }



    private void determineWaterHeightOffsets() {
//        float finalYPosition = getYPositionAtOffset(offsetPosition1);
//        finalYPosition += getYPositionAtOffset(offsetPosition2);
//        finalYPosition += getYPositionAtOffset(offsetPosition3);
//        finalYPosition += getYPositionAtOffset(offsetPosition4);
//        finalYPosition /= 4;
//        setPosition(getPosition().x, finalYPosition, getPosition().z);





        //EntityPositionHelper.getRelativePosition(boatMountedCamera, boatEntity, cameraPositionOffset, cameraRotationOffset);
        Vector3f translatedPosition1 = EntityPositionHelper.getRelativePosition(this, offsetPosition1, new Vector3f(0, 0, 0)).getPosition();
        Vector3f translatedPosition2 = EntityPositionHelper.getRelativePosition(this, offsetPosition2, new Vector3f(0, 0, 0)).getPosition();
        Vector3f translatedPosition3 = EntityPositionHelper.getRelativePosition(this, offsetPosition3, new Vector3f(0, 0, 0)).getPosition();
        Vector3f translatedPosition4 = EntityPositionHelper.getRelativePosition(this, offsetPosition4, new Vector3f(0, 0, 0)).getPosition();

        translatedPosition1.y = boatVis.getRenderedWater().getWaterHeight(translatedPosition1.x, translatedPosition1.z);
        translatedPosition2.y = boatVis.getRenderedWater().getWaterHeight(translatedPosition2.x, translatedPosition2.z);
        translatedPosition3.y = boatVis.getRenderedWater().getWaterHeight(translatedPosition3.x, translatedPosition3.z);
        translatedPosition4.y = boatVis.getRenderedWater().getWaterHeight(translatedPosition4.x, translatedPosition4.z);



        float sumOfXSquared = sumOfCalulation(new VecCalulation() {
            @Override
            public float performCalulation(Vector3f vector3f) {
                return (vector3f.x * vector3f.x);
            }
        }, translatedPosition1, translatedPosition2, translatedPosition3, translatedPosition4);
        float sumOfXZ = sumOfCalulation(new VecCalulation() {
            @Override
            public float performCalulation(Vector3f vector3f) {
                return (vector3f.x * vector3f.z);
            }
        }, translatedPosition1, translatedPosition2, translatedPosition3, translatedPosition4);
        float sumOfX = sumOfCalulation(new VecCalulation() {
            @Override
            public float performCalulation(Vector3f vector3f) {
                return (vector3f.x);
            }
        }, translatedPosition1, translatedPosition2, translatedPosition3, translatedPosition4);
        float sumOfZSquared = sumOfCalulation(new VecCalulation() {
            @Override
            public float performCalulation(Vector3f vector3f) {
                return (vector3f.z * vector3f.z);
            }
        }, translatedPosition1, translatedPosition2, translatedPosition3, translatedPosition4);
        float sumOfZ = sumOfCalulation(new VecCalulation() {
            @Override
            public float performCalulation(Vector3f vector3f) {
                return (vector3f.z);
            }
        }, translatedPosition1, translatedPosition2, translatedPosition3, translatedPosition4);
        float sumOf1 = sumOfCalulation(new VecCalulation() {
            @Override
            public float performCalulation(Vector3f vector3f) {
                return 1;
            }
        }, translatedPosition1, translatedPosition2, translatedPosition3, translatedPosition4);
        float sumOfXY = sumOfCalulation(new VecCalulation() {
            @Override
            public float performCalulation(Vector3f vector3f) {
                return (vector3f.x * vector3f.y);
            }
        }, translatedPosition1, translatedPosition2, translatedPosition3, translatedPosition4);
        float sumOfZY = sumOfCalulation(new VecCalulation() {
            @Override
            public float performCalulation(Vector3f vector3f) {
                return (vector3f.z * vector3f.y);
            }
        }, translatedPosition1, translatedPosition2, translatedPosition3, translatedPosition4);
        float sumOfY = sumOfCalulation(new VecCalulation() {
            @Override
            public float performCalulation(Vector3f vector3f) {
                return (vector3f.y);
            }
        }, translatedPosition1, translatedPosition2, translatedPosition3, translatedPosition4);

        Matrix3f lhs = new Matrix3f();
        lhs.m00 = sumOfXSquared;
        lhs.m10 = sumOfXZ;
        lhs.m20 = sumOfX;
        lhs.m01 = sumOfXZ;
        lhs.m11 = sumOfZSquared;
        lhs.m21 = sumOfZ;
        lhs.m02 = sumOfX;
        lhs.m12 = sumOfZ;
        lhs.m22 = sumOf1;

        lhs.invert();
        Vector3f rhs = new Vector3f(sumOfXY, sumOfZY, sumOfY);

        Vector3f abc = new Vector3f(rhs);
        lhs.transform(abc);

        float a = abc.x;
        float b = abc.y;
        float c = abc.z;
        //y = ax + bz + c
        float boatYPos = ((a * getPosition().x) + (b * getPosition().z) + c);
        PositionRotationUtil3D.setYPosition(this, boatYPos);

        //Now for the rotations
        //y = ax + bz + c
        //1y -ax - bz = c
        //Therefor a normal to the surface is:
        Vector3f surfaceNormal = new Vector3f(-a, 1, -b);



        //TODO - This next part is dodgy and should be revised:
        float xRotation = (float) (Math.atan2(-a, 1) * Math.sin(getRotation().y));
        float zRotation = (float) (Math.atan2(-b, 1) * Math.sin(getRotation().y));
        //PositionRotationUtil3D.setXRotation(this, xRotation);
        //PositionRotationUtil3D.setZRotation(this, zRotation);
    }

    private abstract class VecCalulation {
        public abstract float performCalulation(Vector3f vector3f);
    }

    private float sumOfCalulation(VecCalulation vecCalulation, Vector3f... vector3fs) {
        float totalSum = 0;
        for (Vector3f vector3f : vector3fs) {
            totalSum += vecCalulation.performCalulation(vector3f);
        }
        return totalSum;
    }

    private float getYPositionAtOffset(Vector3f offsetPosition) {
        float xTestPosition = getPosition().x + offsetPosition.x;
        float zTestPosition = getPosition().z + offsetPosition.z;
        return boatVis.getRenderedWater().getWaterHeight(xTestPosition, zTestPosition);
    }

    public Vector3f getVelocity(){
        return currentVelocity;
    }

}
