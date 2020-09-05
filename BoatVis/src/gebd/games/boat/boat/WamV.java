package gebd.games.boat.boat;

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
import gebd.shaders.quat.BasicQuatShader;
import gebd.shaders.quat.Textured3DQuatShader;
import physics.render.PhysRenderHelper;
import physics.util.OffsetHelper;
import physics.util.PhysTransform;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 9/4/2016.
 */
public class WamV {

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

    private Entity wamVModel;
    private Entity boxModel;
    private Entity cylinderModel;

    public static float DEFAULT_LINEAR_DAMPING = 0.3f;
    public static float DEFAULT_ANGULAR_DAMPING = 0.5f;

    private float currentLeftMotorForce = 0f;
    private float currentRightMotorForce = 0f;

    public WamV(float mass, Entity wamVModel, Entity boxModel, Entity cylinderModel, DynamicsWorld dynamicsWorld, @NotNull Transform startTransform) {
        this.mass = mass;
        this.wamVModel = wamVModel;
        this.boxModel = boxModel;
        this.cylinderModel = cylinderModel;
        this.startTransform = startTransform;

        this.compoundShape = setupCompoundShape();
        setupPhysics(dynamicsWorld);
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

    public void renderWamV(Shader3D shader) {
        PhysRenderHelper.setEntityTransform(wamVModel, getWorldTransform());
        wamVModel.render(shader);
    }

    private void renderEntityWithTransform(Transform centerOfObject, Transform offset, Entity entity,
                                           Vector3f size, Shader3D shader) {
        Transform visibleTransform = new Transform();
        visibleTransform.set(centerOfObject);
        visibleTransform.mul(offset);
        PhysRenderHelper.prepareEntity(entity, visibleTransform, size);
        entity.render(shader);
    }

    public void renderPhysicsObjects(Shader3D shader) {
        Transform centerOfObject = getWorldTransform();

        //The payload component:
        renderEntityWithTransform(centerOfObject, payloadTransform, boxModel, payloadSize, shader);
        //The left Pontoon.
        renderEntityWithTransform(centerOfObject, cylinderLeftTransform, cylinderModel, pontoonSize, shader);
        //The right Pontoon.
        renderEntityWithTransform(centerOfObject, cylinderRightTransform, cylinderModel, pontoonSize, shader);
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

    public Entity getWamVModel() {
        PhysRenderHelper.setEntityTransform(wamVModel, getWorldTransform());
        return wamVModel;
    }

    /**
     * Applies the force on the back of the left pontoon.
     * @param leftMotorForce - The force (In Newtons I suppose) to apply.
     */
    public void applyConstantMotorForceOnLeftPontoon(float leftMotorForce) {
        this.currentLeftMotorForce = leftMotorForce;
    }

    /**
     * Applies the force on the back of the right pontoon.
     * @param rightMotorForce - The force (In Newtons I suppose) to apply.
     */
    public void applyConstantMotorForceOnRightPontoon(float rightMotorForce) {
        this.currentRightMotorForce = rightMotorForce;
    }

    public void applyConstantMotorForces() {

        Transform leftCylinderTransform = getLeftCylinderTransform();

        applyForceToBackOfPontoon(getLeftCylinderTransform(), currentLeftMotorForce);
        applyForceToBackOfPontoon(getRightCylinderTransform(), currentRightMotorForce);

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

    public float getCurrentLeftMotorForce() {
        return currentLeftMotorForce;
    }

    public float getCurrentRightMotorForce() {
        return currentRightMotorForce;
    }

    public void setDamping(float linearDamping, float angularDamping) {
        this.body.setDamping(linearDamping, angularDamping);
    }
}
