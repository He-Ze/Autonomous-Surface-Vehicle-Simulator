package gebd.games.boat.physics;

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
import gebd.games.boat.boat.BoatEntity;
import gebd.games.boat.boat.WamV;
import gebd.games.boat.camera.CameraCalculationThread;
import gebd.games.boat.lidar.LidarPoint;
import gebd.games.boat.lidar.LidarReading;
import gebd.shaders.Shader3D;
import physics.buoyancy.CylinderBuoyancy;
import physics.handler.PhysicsHandler;
import physics.util.OffsetHelper;
import physics.util.PhysTransform;
import physics.util.Plane;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;
import java.net.ServerSocket;
import java.util.Scanner;
import java.lang.System;
import gebd.games.boat.lidar.LidarCalculationHandler;
import gebd.games.boat.lidar.LidarHelper;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by CaptainPete on 9/11/2016.
 */
public class BoatVisPhysics extends PhysicsHandler implements DestoryableThread {

    public static final float DENSITY_OF_WATER = 1000f; //KG / m^3
    public static final float GRAVITY = -9.81f;

    public BoatVis boatVis;
    public BoxShape smallerBoxShape;
    public CylinderShape cylinderShape;
    public WamV wamV;

    protected float testCylinderLength = 5f; //m
    protected float testCylinderRadius = 1f; //m
    protected float testCylinderMass = 2500f; //KG
    public static final int numSimulatedCylinderSegments = 20;

    private boolean physicsRunning = true;

    public BoatVisPhysics(BoatVis boatVis) {
        super(GRAVITY);
        this.boatVis = boatVis;
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

        Entity wamVEntity = boatVis.getPhysicsBoatEntity(1,0,0);
        Entity cubeEntity = boatVis.getCubeEntity();
        Entity cylinderEntity = boatVis.getCylinderEntity();
        wamV = new WamV(mass, wamVEntity, cubeEntity, cylinderEntity, dynamicsWorld, startTransform);
    }

    @Override
    public void run() {
        int y=0;
        //Scanner s=new Scanner(System.in);
        new Thread(new Server()).start();
        new Thread(new command()).start();
        while (physicsRunning) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                return;
            }
            updatePhysics(boatVis.getPhysicsSettingsDisplay().getPhysicsSpeedModifier());



//            BoatEntity p;
//            String a=s.nextLine();
//            if(a.equals("position"))
//           if(y%1000==0)
 //               p=boatVis.getPhysicsBoatEntity(2,0,0);
//            y++;

        }

    }
    class Server implements Runnable{
        public static final int PORT = 50520;//监听的端口号
        BoatEntity p;
        @Override
        public void run() {
            System.out.println("服务器启动...\n");
            Server server = new Server();
            server.init();
        }
        public void init() {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                while (true) {
                    // 一旦有堵塞, 则表示服务器与客户端获得了连接
                    Socket client = serverSocket.accept();
                    // 处理这次连接
                    new HandlerThread(client);
                }
            } catch (Exception e) {
                System.out.println("服务器异常: " + e.getMessage());
            }
        }
        private class HandlerThread implements Runnable {
            private Socket socket;
            public HandlerThread(Socket client) {
                socket = client;
                new Thread(this).start();
            }

            public void run() {

                try {
                    // 读取客户端数据
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
                    // 处理客户端数据
                    System.out.println("客户端发过来的内容:" + clientInputStr);

                    // 向客户端回复信息
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    //System.out.print("请输入:\t");
                    // 发送键盘输入的一行
                    //String s = new BufferedReader(new InputStreamReader(System.in)).readLine();

                    String[] s1 = clientInputStr.split("\\s+");
                    if(s1[0].equals("position")) {
                        p = boatVis.getPhysicsBoatEntity(2, 0, 0);
                        out.writeUTF(p.getPosition().toString());
                    }
                    else if(s1[0].equals("speed")) {
                        p = boatVis.getPhysicsBoatEntity(3, 0, 0);
                        out.writeUTF(boatVis.getCurrentBoatVelocity().toString());
                    }
                    else if(s1[0].equals("distance")) {
                        p = boatVis.getPhysicsBoatEntity(4, 0, 0);
                        String ff = Float.toString(p.getPosition().length());
                        out.writeUTF(ff);
                    }
                    else if(s1[0].equals("rotation")) {
                        p = boatVis.getPhysicsBoatEntity(5, 0, 0);
                        out.writeUTF(p.getQuatRotation().toString());
                    }
                    else if(s1[0].equals("pos_and_spe")) {
                        p = boatVis.getPhysicsBoatEntity(12, 0, 0);
                        String ff= Float.toString(p.getPosition().x) +","+p.getPosition().y+","+p.getPosition().z+","+boatVis.getCurrentBoatVelocity().x+","+boatVis.getCurrentBoatVelocity().y+","+boatVis.getCurrentBoatVelocity().z;
                        out.writeUTF(ff);
                    }
                    else if(s1[0].equals("setleft")) {
                        p = boatVis.getPhysicsBoatEntity(6,Integer.parseInt(s1[1]),0);
                        String s="The left motor's force has changed to "+Float.toString(Integer.parseInt(s1[1]))+"N";
                        out.writeUTF(s);
                    }
                    else if(s1[0].equals("setright")) {
                        p = boatVis.getPhysicsBoatEntity(7,Integer.parseInt(s1[1]),0);
                        String s="The right motor's force has changed to "+Float.toString(Integer.parseInt(s1[1]))+"N";
                        out.writeUTF(s);
                    }
                    else if(s1[0].equals("setall")) {
                        p = boatVis.getPhysicsBoatEntity(8,Integer.parseInt(s1[1]),0);
                        String s="Both motor's force has changed to "+Float.toString(Integer.parseInt(s1[1]))+"N";
                        out.writeUTF(s);
                    }
                    else if(s1[0].equals("reset")) {
                        p = boatVis.getPhysicsBoatEntity(9,0,0);
                        out.writeUTF("Reset finished!");
                    }
                    else if(s1[0].equals("setspeed")) {
                        p = boatVis.getPhysicsBoatEntity(10,Integer.parseInt(s1[1]),Integer.parseInt(s1[2]));
                        String s="The left and right motors's force have changed to "+Float.toString(Integer.parseInt(s1[1]))+"N and "+Float.toString(Integer.parseInt(s1[2]))+"N";
                        out.writeUTF(s);
                    }
                    else if(s1[0].equals("lidar")) {
                        boatVis.getPhysicsBoatEntity2(11,Float.parseFloat(s1[1]),Float.parseFloat(s1[2]));
                        String ff = Float.toString(LidarCalculationHandler.getDepthAtPixelPercentages(Float.parseFloat(s1[1]), Float.parseFloat(s1[2])));
                        out.writeUTF(ff);
                    }
                    else if(s1[0].equals("camera")) {
                        boatVis.getPhysicsBoatEntity2(12,Float.parseFloat(s1[1]),Float.parseFloat(s1[2]));
                        int qq=(int)(640*Float.parseFloat(s1[1]));
                        int pp=(int)(480*Float.parseFloat(s1[2]));
                        out.writeUTF(CameraCalculationThread.getPixelAt(qq, pp).toString());
                    }
                    else if(s1[0].equals("lidarpoint")) {
                        new LidarPoint (Double.parseDouble(s1[1]),Double.parseDouble(s1[2]));
                        String ff = LidarPoint.getXYZ();
                        out.writeUTF(ff);
                    }


                    //out.writeUTF(s);

                    out.close();
                    input.close();
                } catch (Exception e) {
                    System.out.println("服务器 run 异常: " + e.getMessage());
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (Exception e) {
                            socket = null;
                            System.out.println("服务端 finally 异常:" + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    class command implements Runnable{
        BoatEntity p;
        @Override
        public void run() {
            Scanner s=new Scanner(System.in);
            while(true){
                //p=boatVis.getPhysicsBoatEntity(11,0,0);
                String a=s.nextLine();
                String[] s1 = a.split("\\s+");
                if(s1[0].equals("position"))
                    p=boatVis.getPhysicsBoatEntity(2,0,0);
                else if(s1[0].equals("speed"))
                    p=boatVis.getPhysicsBoatEntity(3,0,0);
                else if(s1[0].equals("distance"))
                    p=boatVis.getPhysicsBoatEntity(4,0,0);
                else if(s1[0].equals("rotation"))
                    p=boatVis.getPhysicsBoatEntity(5,0,0);
	else if(s1[0].equals("pos_and_spe"))
                    p=boatVis.getPhysicsBoatEntity(12,0,0);
                else if(s1[0].equals("setleft")) {
                    p = boatVis.getPhysicsBoatEntity(6,Integer.parseInt(s1[1]),0);
                }
                else if(s1[0].equals("setright")) {
                    p = boatVis.getPhysicsBoatEntity(7,Integer.parseInt(s1[1]),0);
                }
                else if(s1[0].equals("setall")) {
                    p = boatVis.getPhysicsBoatEntity(8,Integer.parseInt(s1[1]),0);
                }
                else if(s1[0].equals("reset")) {
                    p = boatVis.getPhysicsBoatEntity(9,0,0);
                }
                else if(s1[0].equals("setspeed")) {
                    p = boatVis.getPhysicsBoatEntity(10,Integer.parseInt(s1[1]),Integer.parseInt(s1[2]));
                }
                else if(s1[0].equals("lidar")) {
                    boatVis.getPhysicsBoatEntity2(11,Float.parseFloat(s1[1]),Float.parseFloat(s1[2]));
                }
                else if(s1[0].equals("camera")) {
                    boatVis.getPhysicsBoatEntity2(12,Float.parseFloat(s1[1]),Float.parseFloat(s1[2]));
                }
                else if(s1[0].equals("lidarpoint")) {
                    new LidarPoint (Double.parseDouble(s1[1]),Double.parseDouble(s1[2]));
                    String ff = LidarPoint.getXYZ();
                    System.out.printf(ff);
                }

                else
                    System.out.printf("Wrong command!");
            }

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

    private void handleWaterForcesOnPontoon(Transform pontoonWorldTransform, WamV wamV) {

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
                            currentOffset, boatVis.getRenderedWater(),
                            DENSITY_OF_WATER, getGravity()));
            wamVBody.applyForce(buoyancyForce, relativePosition);
//                wamVBody.applyCentralForce(buoyancyForce);

        }

    }

    public void renderPhysics(Shader3D shader) {
        //TODO - Implement fully.
        if (boatVis.getPhysicsSettingsDisplay().usePhysicsBoat()) {
            wamV.renderPhysicsObjects(shader);
            wamV.renderWamV(shader);
        }
    }

    public WamV getWamV() {
        return wamV;
    }

    @Override
    public void destroyThread() {
        physicsRunning = false;
    }

}
