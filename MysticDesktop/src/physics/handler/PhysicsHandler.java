package physics.handler;

import com.bulletphysics.BulletStats;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.Clock;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.util.ObjectArrayList;

/**
 * Created by CaptainPete on 9/11/2016.
 */
public abstract class PhysicsHandler {

    // keep the collision shapes, for deletion/cleanup
    protected  DynamicsWorld dynamicsWorld;
    protected ObjectArrayList<CollisionShape> collisionShapes = new ObjectArrayList<CollisionShape>();
    private BroadphaseInterface broadphase;
    private CollisionDispatcher dispatcher;
    private ConstraintSolver solver;
    private DefaultCollisionConfiguration collisionConfiguration;

    private Clock clock;

    private float gravity;

    private boolean physicsRunning = true;

    public PhysicsHandler(float gravity) {
        this.gravity = gravity;
    }

    protected abstract void addPhysicsObjectsToScene();

    /**
     * This is called after a single step has taken place in the bullet physics.
     * Ordinarily, nothing should be done, however in terms of buoyancy simulation
     * it's probably best to update based on
     */
    protected void beforeSingleStep(){

    }

    protected void afterSingleStep(){

    }

    public boolean isPhysicsRunning() {
        return physicsRunning;
    }

    public void setPhysicsRunning(boolean physicsRunning) {
        this.physicsRunning = physicsRunning;
    }

    public float getGravity() {
        return gravity;
    }

    public float getDeltaTimeMicroseconds() {
        //#ifdef USE_BT_CLOCK
        float dt = clock.getTimeMicroseconds();
        clock.reset();
        return dt;
        //#else
        //return btScalar(16666.);
        //#endif
    }

    public void updatePhysics(float timestepMultiplier) {
        // simple dynamics world doesn't handle fixed-time-stepping
        float ms = getDeltaTimeMicroseconds();
        float timeStep = ms / 1000000f;
        timeStep *= timestepMultiplier;
        updatePhysicsSingleStep(timeStep);
    }

    public void updatePhysicsSingleStep(float timestep) {
        // simple dynamics world doesn't handle fixed-time-stepping
        if (dynamicsWorld != null && physicsRunning) {
            dynamicsWorld.stepSimulation(timestep);
        }
    }

    public void initPhysics() {
        clock = new Clock();
        //setCameraDistance(50f);

        // collision configuration contains default setup for memory, collision setup
        collisionConfiguration = new DefaultCollisionConfiguration();

        // use the default collision dispatcher. For parallel processing you can use a diffent dispatcher (see Extras/BulletMultiThreaded)
        dispatcher = new CollisionDispatcher(collisionConfiguration);

        broadphase = new DbvtBroadphase();

        // the default constraint solver. For parallel processing you can use a different solver (see Extras/BulletMultiThreaded)
        SequentialImpulseConstraintSolver sol = new SequentialImpulseConstraintSolver();
        solver = sol;

        // TODO: needed for SimpleDynamicsWorld
        //sol.setSolverMode(sol.getSolverMode() & ~SolverMode.SOLVER_CACHE_FRIENDLY.getMask());

        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration){
            @Override
            protected void internalSingleStepSimulation(float timeStep) {
                beforeSingleStep();
                super.internalSingleStepSimulation(timeStep);
                afterSingleStep();
            }
        };

        dynamicsWorld.setGravity(new javax.vecmath.Vector3f(0f, gravity, 0f));

        addPhysicsObjectsToScene();

        clientResetScene();
    }


    public void clientResetScene() {
        //#ifdef SHOW_NUM_DEEP_PENETRATIONS
        BulletStats.gNumDeepPenetrationChecks = 0;
        BulletStats.gNumGjkChecks = 0;
        //#endif //SHOW_NUM_DEEP_PENETRATIONS

        int numObjects = 0;
        if (dynamicsWorld != null) {
            dynamicsWorld.stepSimulation(1f / 60f, 0);
            numObjects = dynamicsWorld.getNumCollisionObjects();
        }

        for (int i = 0; i < numObjects; i++) {
            CollisionObject colObj = dynamicsWorld.getCollisionObjectArray().getQuick(i);
            RigidBody body = RigidBody.upcast(colObj);
            if (body != null) {
                if (body.getMotionState() != null) {
                    DefaultMotionState myMotionState = (DefaultMotionState) body.getMotionState();
                    myMotionState.graphicsWorldTrans.set(myMotionState.startWorldTrans);
                    colObj.setWorldTransform(myMotionState.graphicsWorldTrans);
                    colObj.setInterpolationWorldTransform(myMotionState.startWorldTrans);
                    colObj.activate();
                }
                // removed cached contact points
                dynamicsWorld.getBroadphase().getOverlappingPairCache().cleanProxyFromPairs(colObj.getBroadphaseHandle(), dynamicsWorld.getDispatcher());

                body = RigidBody.upcast(colObj);
                if (body != null && !body.isStaticObject()) {
                    RigidBody.upcast(colObj).setLinearVelocity(new javax.vecmath.Vector3f(0f, 0f, 0f));
                    RigidBody.upcast(colObj).setAngularVelocity(new javax.vecmath.Vector3f(0f, 0f, 0f));
                }
            }

			/*
            //quickly search some issue at a certain simulation frame, pressing space to reset
			int fixed=18;
			for (int i=0;i<fixed;i++)
			{
			getDynamicsWorld()->stepSimulation(1./60.f,1);
			}
			*/
        }
    }

}
