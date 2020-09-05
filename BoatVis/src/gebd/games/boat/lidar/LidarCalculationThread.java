package gebd.games.boat.lidar;

import gebd.Render;
import gebd.camera.Camera;
import gebd.concurrent.DestoryableThread;
import gebd.concurrent.ThreadDestroyer;
import org.lwjgl.opengl.GL11;
import renderables.framebuffer.FrameBufferObject;

import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

/**
 * Created by CaptainPete on 8/9/2016.
 */
public class LidarCalculationThread implements DestoryableThread {

    private boolean running = true;
    private Thread runningThread = null;

    private Semaphore bufferSwitchLock = new Semaphore(1);
    private Semaphore newBufferReadyLock = new Semaphore(0);
    private LidarHelper lidarHelper;

    private static final int BYTES_PER_PIXEL = 3; //RGB.
    private static final float MAX_DEPTH_INT_VALUE = 256.0f * 256.0f * 256.0f;

    LidarCalculationHandler lidarCalculationHandler = null;

    public LidarCalculationThread(LidarHelper lidarHelper, LidarHelper.DepthBufferDirection depthBufferDirection) {
        this.lidarHelper = lidarHelper;

        //Start up the Thread.
        runningThread = new Thread(this);
        runningThread.start();
    }

    @Override
    public void run() {
        ThreadDestroyer.registerDestroyableThread(this);
        while (running) {
            try {
                newBufferReadyLock.acquire();
            } catch (InterruptedException e) {
                //Boat vis is shutting down.
                return;
            }

            if (lidarCalculationHandler == null) {
                //There is currently no LidarCalcuationHandler.
                continue;
            }

            bufferSwitchLock.acquireUninterruptibly();
            lidarCalculationHandler.switchReadAndWaitBuffers();
            bufferSwitchLock.release();

            //Read the current read buffer.
            //lidarCalculationHandler.readCurrentBuffer();
            lidarCalculationHandler.readCurrentBuffers();

        }
    }

    public void addBufferInformation(Camera lidarCamera, LidarCalculationHandler newLidarCalculationHandler) {
        this.lidarCalculationHandler = newLidarCalculationHandler;

        if (this.lidarCalculationHandler == null) {
            //There is currently no LidarCalcuationHandler.
            return;
        }

        //lidarCamera.getProjectionMatrixHandler().setFieldOfView(90f);
        //lidarCamera.getProjectionMatrixHandler().setAspectRatio((float) Math.random() * 2f);
        //lidarCamera.getProjectionMatrixHandler().setAspectRatio(1f);

        //Read the data into the buffer.
        lidarCalculationHandler.readNewDepthData(lidarCamera);

        bufferSwitchLock.acquireUninterruptibly();

        //Switch the buffers around.
        lidarCalculationHandler.switchWriteAndWaitBuffer();
        bufferSwitchLock.release();
        //Notify the thread that it's ready to perform read buffer operations.
        newBufferReadyLock.release();
    }

    @Override
    public void destroyThread() {
        running = false;
        if (runningThread != null) {
            //Stop the thread!
            runningThread.interrupt();
        }
        newBufferReadyLock.release();
    }

    public static LidarCalculationHandler[] generateLidarAtHz(float revolutionsPerSecond, LidarHelper lidarHelper) {
        int numCalculationHandlers = LidarHelper.DepthBufferDirection.values().length;
        LidarCalculationHandler[] result = new LidarCalculationHandler[numCalculationHandlers];
        for (int i = 0; i < numCalculationHandlers; i++) {
            LidarHelper.DepthBufferDirection direction = LidarHelper.DepthBufferDirection.values()[i];
            result[i] = new LidarCalculationHandler(direction, revolutionsPerSecond, lidarHelper);
        }
        return result;
    }
}
