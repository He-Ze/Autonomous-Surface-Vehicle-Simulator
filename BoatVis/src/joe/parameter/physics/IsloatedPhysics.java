package joe.parameter.physics;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CylinderShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import composites.entities.Entity;
import gebd.concurrent.DestoryableThread;
import gebd.games.boat.BoatVis;
import gebd.games.boat.boat.WamV;
import gebd.shaders.Shader3D;
import physics.buoyancy.CylinderBuoyancy;
import physics.handler.PhysicsHandler;
import physics.util.OffsetHelper;
import physics.util.PhysTransform;
import physics.util.Plane;
import renderables.r3D.water.RenderedWater;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 9/11/2016.
 */
public class IsloatedPhysics extends PhysicsHandler implements DestoryableThread {

    public static final float DENSITY_OF_WATER = 1000f; //KG / m^3
    public static final float GRAVITY = -9.81f;

    private RenderedWater water;
    private BoxShape smallerBoxShape;
    private CylinderShape cylinderShape;
    private NonRenderedWamV wamV;

    protected float testCylinderLength = 5f; //m
    protected float testCylinderRadius = 1f; //m
    protected float testCylinderMass = 2500f; //KG
    public static final int numSimulatedCylinderSegments = 20;

    private boolean physicsRunning = true;

    private float updateTimeStep;

    public IsloatedPhysics(RenderedWater water, float updateTimeStep) {
        super(GRAVITY);
        this.water = water;
        this.updateTimeStep = updateTimeStep;
    }

    @Override
    protected void addPhysicsObjectsToScene() {

        //Adds a box the water surface.
        addSeaFloor();

        // create a few dynamic rigidbodies
        // Re-using the same collision is better for memory usage and performance

        smallerBoxShape = new BoxShape(new Vector3f(1, 1, 1));
        collisionShapes.add(smallerBoxShape);
        cylinderShape = new CylinderShape(new Vector3f(testCylinderRadius, testCylinderLength / 2.0f, testCylinderRadius));
        collisionShapes.add(cylinderShape);
        //CollisionShape colShape = new SphereShape(1f);

            /*
            Transform transformTest = new Transform();
            //transformTest.origin.set();
            Quat4f quarternion = new Quat4f();
            Quaternion quaternion = new Quaternion();
            //quarternion.setEuler();
            CompoundShape compoundShape = new CompoundShape();
            //compoundShape.addChildShape();
            */

        // Create Dynamic Objects
        Transform startTransform = new Transform();
        startTransform.setIdentity();

        float mass = testCylinderMass; //200KG.Y

        // rigidbody is dynamic if and only if mass is non zero, otherwise static
        boolean isDynamic = (mass != 0f);

        Vector3f localInertia = new Vector3f(0, 0, 0);
        if (isDynamic) {
            smallerBoxShape.calculateLocalInertia(mass, localInertia);
            cylinderShape.calculateLocalInertia(mass, localInertia);
        }


//            startTransform.origin.set(0, -4, 0);
//            addPhysBoxToScene(startTransform, mass, localInertia);
//            //startTransform.origin.set(1.1f, 0, 0);
//            startTransform.origin.set(0.0f, 0, 1.1f);
//            addPhysBoxToScene(startTransform, mass, localInertia);
        ///*

        startTransform.origin.set(-0.5f, 10f -0.5f, -0.5f);

        startTransform.origin.set(0f, 1f, 0f);

        Vector3f vecTest = new Vector3f(1, 1, 1);
        vecTest.normalize();
        Vector3f vecTest2 = new Vector3f(0, 1, 0);
        vecTest2.normalize();
        //|A.B| = |A||B|cos(theata)
        float rotationAmount = (float) (Math.acos(vecTest.dot(vecTest2)));
        //Quat4f rotation = new Quat4f();
        //float rotationAmount = (float) (-Math.PI / 4.0);
        //AxisAngle4f rotation = new AxisAngle4f(1f, 1f, 0f, rotationAmount);
        //AxisAngle4f rotation = new AxisAngle4f(1f, 0f, -1f, (float) Math.PI / 4.0f);

        //AxisAngle4f rotation = Plane.rotatePlaneSoUpIsNowOtherThing(new Vector3f(0, 1, 0), new Vector3f(-1, 1, -1));
        AxisAngle4f rotation = Plane.rotatePlaneSoUpIsNowOtherThing(new Vector3f(0, 1, 0), new Vector3f(1, 0, 1));

        startTransform.basis.set(PhysTransform.toPhysAngle(rotation));

        addPhysFloatingCylinderToScene(startTransform, mass, localInertia);

        //addCompositeObjectToScene();
        addWamVToScene();
        //*/

    }


    private void addSeaFloor() {
        // create a few basic rigid bodies
        CollisionShape groundShape = new BoxShape(new Vector3f(50f, 50f, 50f));
        //CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 50);

        collisionShapes.add(groundShape);

        Transform groundTransform = new Transform();
        groundTransform.setIdentity();
        groundTransform.origin.set(0, -53, 0);

        // We can also use DemoApplication::localCreateRigidBody, but for clarity it is provided here:
        {
            float mass = 0f;

            // rigidbody is dynamic if and only if mass is non zero, otherwise static
            boolean isDynamic = (mass != 0f);

            Vector3f localInertia = new Vector3f(0, 0, 0);
            if (isDynamic) {
                groundShape.calculateLocalInertia(mass, localInertia);
            }

            // using motionstate is recommended, it provides interpolation capabilities, and only synchronizes 'active' objects
            DefaultMotionState myMotionState = new DefaultMotionState(groundTransform);
            RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, groundShape, localInertia);
            RigidBody body = new RigidBody(rbInfo);

            //body.checkCollideWithOverride(null);
            //body.checkCollideWith(null)
            //dynamicsWorld.getPairCache().findPair();
            //dynamicsWorld.
            //body.

            //body.getCollisionShape().
            body.getCcdMotionThreshold();

            // add the body to the dynamics world
            dynamicsWorld.addRigidBody(body);
        }
    }

    private void addPhysFloatingCylinderToScene(Transform startTransform, float mass, Vector3f localInertia) {
        // using motionstate is recommended, it provides interpolation capabilities, and only synchronizes 'active' objects
        DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
        //RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, smallerBoxShape, localInertia);
        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, cylinderShape, localInertia);


        RigidBody body = new RigidBody(rbInfo);
        body.setActivationState(RigidBody.DISABLE_DEACTIVATION);
        body.setDamping(0.50f, 0.50f);
//        body.setDamping(0.20f, 0.20f);

        dynamicsWorld.addRigidBody(body);
        //someRigidBodies.add(body);
    }

    private void addWamVToScene() {
        float mass = 255f; //KG.

        Transform startTransform = new Transform();
        //startTransform.origin.set(5, 0.77f, 0);
        startTransform.origin.set(PhysTransform.toPhysPosition(new Vector3f(5, 0.77f, 0)));
        float angle = (float) (Math.PI / 3.0);
        startTransform.basis.set(new AxisAngle4f(0f, 1f, 0.0f, angle));

        wamV = new NonRenderedWamV(mass, dynamicsWorld, startTransform);
    }

    @Override
    public void run() {

        while (physicsRunning) {

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                return;
            }

            updatePhysics(updateTimeStep);

        }

    }

    @Override
    protected void beforeSingleStep() {
        super.beforeSingleStep();
        //Handle buoyancy forces.
        handleWaterForcesOnWamV();
        //Handle motor forces.
        wamV.applyConstantMotorForces();
    }

    @Override
    protected void afterSingleStep() {
        super.afterSingleStep();
        //Clear all previous forces.
        wamV.getBody().clearForces();
    }

    private void handleWaterForcesOnWamV() {
        //Handle the water forces on both cylinders.
        handleWaterForcesOnPontoon(wamV.getLeftCylinderTransform(), wamV);
        handleWaterForcesOnPontoon(wamV.getRightCylinderTransform(), wamV);
    }

    private void handleWaterForcesOnPontoon(Transform pontoonWorldTransform, NonRenderedWamV wamV) {

        RigidBody wamVBody = wamV.getBody();
        Transform currentPhysicsTransform = new Transform();
        Vector3f wamVWorldLocation = wamVBody.getWorldTransform(currentPhysicsTransform).origin;

        float cylinderRadius = wamV.getPontoonCylnderRadius();
        float lengthOfCylinder = wamV.getPontoonCylinderLength();
        float halfCylinderLength = lengthOfCylinder / 2.0f;
        float segmentLength = lengthOfCylinder / ((float) numSimulatedCylinderSegments);
        float halfSegmentLength = segmentLength / 2.0f;

        //cylinderSegment
        //for (cylinderSegment = 0; cylinderSegment < numCylinderSegments; cylinderSegment++) {
        for (int cylinderSegment = 0; cylinderSegment < numSimulatedCylinderSegments; cylinderSegment++) {

            float currentOffset = (((float) cylinderSegment) / ((float) numSimulatedCylinderSegments) * lengthOfCylinder) - halfCylinderLength + halfSegmentLength;

            Vector3f offsetToCenterOfCircle = new Vector3f(0, currentOffset, 0);
            Vector3f physicsCenterOfCircle = OffsetHelper.getOffset(pontoonWorldTransform, offsetToCenterOfCircle);
            Vector3f relativePosition = new Vector3f();
            relativePosition.set(physicsCenterOfCircle);
            relativePosition.sub(wamVWorldLocation);
//                relativePosition.set(wamVWorldLocation);
//                relativePosition.sub(physicsCenterOfCircle);

            Vector3f buoyancyForce = PhysTransform.toPhysPosition(
                    CylinderBuoyancy.getBouyancyForceOnCylinder(
                            pontoonWorldTransform, cylinderRadius, segmentLength,
                            currentOffset, water,
                            DENSITY_OF_WATER, getGravity()));
            wamVBody.applyForce(buoyancyForce, relativePosition);
//                wamVBody.applyCentralForce(buoyancyForce);

        }

    }


    public NonRenderedWamV getWamV() {
        return wamV;
    }

    @Override
    public void destroyThread() {
        physicsRunning = false;
    }

}
