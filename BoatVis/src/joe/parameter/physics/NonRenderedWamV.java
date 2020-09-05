package joe.parameter.physics;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.collision.shapes.CylinderShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.sun.istack.internal.NotNull;
import composites.entities.Entity;
import gebd.shaders.Shader3D;
import physics.render.PhysRenderHelper;
import physics.util.OffsetHelper;
import physics.util.PhysTransform;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 9/4/2016.
 */
public class NonRenderedWamV {

    private Transform payloadTransform = new Transform();
    private Transform cylinderLeftTransform = new Transform();
    private Transform cylinderRightTransform = new Transform();
    private float mass;
    private Vector3f payloadSize = new Vector3f();
    private Vector3f pontoonSize = new Vector3f();
    private Transform startTransform;
    private Vector3f localInertia = new Vector3f(0, 0, 0);

    private CompoundShape compoundShape;
    private RigidBody body;

    public static float DEFAULT_LINEAR_DAMPING = 0.3f;
    public static float DEFAULT_ANGULAR_DAMPING = 0.5f;

    private float currentLeftMotorPercentage = 0f;
    private float currentRightMotorPercentage = 0f;

    private float forwardThrustMultiplier = 1f;
    private float backwardThrustMultiplier = 1f;

    public NonRenderedWamV(float mass, DynamicsWorld dynamicsWorld, @NotNull Transform startTransform) {
        this.mass = mass;
        this.startTransform = startTransform;

        this.compoundShape = setupCompoundShape();
        setupPhysics(dynamicsWorld);
    }

    public void updateParameters(float forwardThrustMultiplier, float backwardThrustMultiplier, float linearDamping, float angularDamping){
        this.forwardThrustMultiplier = forwardThrustMultiplier;
        this.backwardThrustMultiplier = backwardThrustMultiplier;
        body.setDamping(linearDamping, angularDamping);
    }

    private void setupPhysics(DynamicsWorld dynamicsWorld) {

        //Calculate the Inertia (Required for the shape to rotate).
        compoundShape.calculateLocalInertia(mass, localInertia);

        // using motionstate is recommended, it provides interpolation capabilities, and only synchronizes 'active' objects
        DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, compoundShape, localInertia);

        body = new RigidBody(rbInfo);
        body.setActivationState(RigidBody.DISABLE_DEACTIVATION);
        body.setDamping(DEFAULT_LINEAR_DAMPING, DEFAULT_ANGULAR_DAMPING);

//        Transform centerOfMassTransform = new Transform();
//        body.setCenterOfMassTransform(centerOfMassTransform);

        dynamicsWorld.addRigidBody(body);
    }

    private CompoundShape setupCompoundShape() {
        CompoundShape compoundPhysicsShape = new CompoundShape();

        //Payload Shape:
        payloadSize = PhysTransform.toPhysPosition(new Vector3f(0.52f, 0.039f, 0.915f));
        BoxShape payloadBoxShape = new BoxShape(payloadSize);

        //Payload Component:
        payloadTransform.origin.set(PhysTransform.toPhysPosition(new Vector3f(0.0f, 0.33f, 0.3f)));
        payloadTransform.basis.set(PhysTransform.toPhysAngle(new AxisAngle4f(0, 1, 0, 0f)));
        compoundPhysicsShape.addChildShape(payloadTransform, payloadBoxShape);

        //Cylinder Shape:
        pontoonSize = PhysTransform.toPhysPosition(new Vector3f(0.213f, 1.50f, 0.213f));
        CylinderShape pontoonCylinderShape = new CylinderShape(pontoonSize);

        float distanceForwardFromCenterOfMass = 0.2f;
        float heightAboveCenterOfMass = -0.72f;
        float horizontalDistanceToCenterOfMass = 1.0f;

        //Left Pontoon Component:
        cylinderLeftTransform.origin.set(PhysTransform.toPhysPosition(new Vector3f(-horizontalDistanceToCenterOfMass, heightAboveCenterOfMass, distanceForwardFromCenterOfMass)));
        cylinderLeftTransform.basis.set(PhysTransform.toPhysAngle(new AxisAngle4f(1, 0, 0, (float) (Math.PI / 2.0))));
        compoundPhysicsShape.addChildShape(cylinderLeftTransform, pontoonCylinderShape);

        //Right Pontoon Component:
        cylinderRightTransform.origin.set(PhysTransform.toPhysPosition(new Vector3f(horizontalDistanceToCenterOfMass, heightAboveCenterOfMass, distanceForwardFromCenterOfMass)));
        cylinderRightTransform.basis.set(PhysTransform.toPhysAngle(new AxisAngle4f(1, 0, 0, (float) (Math.PI / 2.0))));
        compoundPhysicsShape.addChildShape(cylinderRightTransform, pontoonCylinderShape);

        return compoundPhysicsShape;
    }

    private void renderEntityWithTransform(Transform centerOfObject, Transform offset, Entity entity,
                                           Vector3f size, Shader3D shader) {
        Transform visibleTransform = new Transform();
        visibleTransform.set(centerOfObject);
        visibleTransform.mul(offset);
        PhysRenderHelper.prepareEntity(entity, visibleTransform, size);
        entity.render(shader);
    }

    public RigidBody getBody() {
        return body;
    }

    public Transform getWorldTransform() {
        Transform centerOfObject = new Transform();
        body.getMotionState().getWorldTransform(centerOfObject);
        return centerOfObject;
    }

    public Transform getPhysicsTransform() {
        Transform centerOfObject = new Transform();
        body.getWorldTransform(centerOfObject);
        return centerOfObject;
    }

    public Transform getLeftCylinderTransform() {
        Transform visibleTransform = new Transform();
        visibleTransform.set(getPhysicsTransform());
        visibleTransform.mul(cylinderLeftTransform);
        return visibleTransform;
    }

    public Transform getRightCylinderTransform() {
        Transform visibleTransform = new Transform();
        visibleTransform.set(getPhysicsTransform());
        visibleTransform.mul(cylinderRightTransform);
        return visibleTransform;
    }

    public float getPontoonCylinderLength() {
        return pontoonSize.y * 2f;
    }

    public float getPontoonCylnderRadius() {
        return pontoonSize.x;
    }

    /**
     * Applies the force on the back of the left pontoon.
     * @param leftMotorForce - The force (In Newtons I suppose) to apply.
     */
    public void applyConstantMotorForceOnLeftPontoon(float leftMotorForce) {
        this.currentLeftMotorPercentage = leftMotorForce;
    }

    /**
     * Applies the force on the back of the right pontoon.
     * @param rightMotorForce - The force (In Newtons I suppose) to apply.
     */
    public void applyConstantMotorForceOnRightPontoon(float rightMotorForce) {
        this.currentRightMotorPercentage = rightMotorForce;
    }

    public void applyConstantMotorForces() {

        applyForceToBackOfPontoon(getLeftCylinderTransform(), applyThrustMutiplier(currentLeftMotorPercentage));
        applyForceToBackOfPontoon(getRightCylinderTransform(), applyThrustMutiplier(currentRightMotorPercentage));

    }

    private float applyThrustMutiplier(float percentage){
        float resultantThrust = percentage;
        if(resultantThrust > 0){
            resultantThrust *= forwardThrustMultiplier;
        } else {
            resultantThrust *= backwardThrustMultiplier;
        }
        return resultantThrust;
    }

    private void applyForceToBackOfPontoon(Transform pontoonTransform, float force) {
        float lengthOfCylinder = getPontoonCylinderLength();
        float halfCylinderLength = lengthOfCylinder / 2.0f;

        Vector3f offsetToCenterOfCircle = new Vector3f(0, halfCylinderLength, 0);
        Vector3f physicsCenterOfCylinder = pontoonTransform.origin;
        Vector3f physicsCenterOfCircle = OffsetHelper.getOffset(pontoonTransform, offsetToCenterOfCircle);
        Vector3f relativePosition = new Vector3f();
        relativePosition.set(physicsCenterOfCircle);
        relativePosition.sub(getPhysicsTransform().origin);

        Vector3f relativeForce = new Vector3f();
        relativeForce.set(physicsCenterOfCircle);
        relativeForce.sub(physicsCenterOfCylinder);
        relativeForce.normalize();
        relativeForce.scale(force);

        body.applyForce(relativeForce, relativePosition);
    }

    public Vector3f getVelocity(){
        Vector3f result = new Vector3f();
        body.getLinearVelocity(result);
        return result;
    }

    public float getCurrentLeftMotorPercentage() {
        return currentLeftMotorPercentage;
    }

    public float getCurrentRightMotorPercentage() {
        return currentRightMotorPercentage;
    }

    public void setDamping(float linearDamping, float angularDamping) {
        this.body.setDamping(linearDamping, angularDamping);
    }
}
