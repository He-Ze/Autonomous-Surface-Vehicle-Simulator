package gebd.games.boat.lidar;

import gebd.camera.Camera;
import gebd.concurrent.DestoryableThread;
import gebd.concurrent.ThreadDestroyer;
import org.lwjgl.opengl.GL11;
import renderables.framebuffer.FrameBufferObject;

import javax.vecmath.Vector2f;
import java.awt.*;
import java.awt.geom.Point2D;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Created by CaptainPete on 8/9/2016.
 */
public class LidarCalculationHandler {

    public FrameBufferObject frameBufferObject;

    public static ByteBuffer currentRead;
    public ByteBuffer currentWrite;
    public ByteBuffer currentWait;
    //private float[] currentDepthMap; //No longer required.

    public static int depthTextureWidth;
    public static int depthTextureHeight;
    public LidarHelper.DepthBufferDirection depthBufferDirection;

    public static  Camera lidarCamera = null;

    public static final int BYTES_PER_PIXEL = 3; //RGB.
    public static final float MAX_DEPTH_INT_VALUE = 256.0f * 256.0f * 256.0f;


    //Measurement range 70 m (1 m to 70 m)
    public static final float MIN_DEPTH = 1.0f;
    public static final float MAX_DEPTH = 70.0f;

    public LidarHelper lidarHelper;

    public LidarCalculationHandler(LidarHelper.DepthBufferDirection depthBufferDirection, float rotationSpeed, LidarHelper lidarHelper) {
        this.depthBufferDirection = depthBufferDirection;
        this.lidarHelper = lidarHelper;

        Vector2f bufferDimensions = getRequiredDimensionsAtRevolutionSpeed(rotationSpeed);
        //Allocate the memory for all the buffers.
        setTextureSize((int) bufferDimensions.x, (int) bufferDimensions.y);
    }

    public static float getDepthAtPixelPercentages(float pixelPercentageWidth, float pixelPercentageHeight) {

        int pixelX = (int) (pixelPercentageWidth * ((float) depthTextureWidth));
        int pixelY = (int) (pixelPercentageHeight * ((float) depthTextureHeight));

        double farPlane = lidarCamera.getProjectionMatrixHandler().getFarPlane();

        int pixelIndex = (pixelY * depthTextureWidth) + pixelX;
        int rgbStartIndex = pixelIndex * BYTES_PER_PIXEL;
        currentRead.position(rgbStartIndex);
        int redRaw = currentRead.get();
        int greenRaw = currentRead.get();
        int blueRaw = currentRead.get();
        int intBits = ((redRaw & 0xFF) << 16)
                + ((greenRaw & 0xFF) << 8)
                + (blueRaw & 0xFF);


        float lidarDistance = (float) ((((float) intBits) / MAX_DEPTH_INT_VALUE) * farPlane);
        if ((lidarDistance > MAX_DEPTH) || (lidarDistance < MIN_DEPTH)) {
            //Limit depth. where 0.0f indicates NO READING.
            lidarDistance = 0.0f;
        }
        return lidarDistance;
    }

    private void setTextureSize(int depthTextureWidth, int depthTextureHeight) {
        this.depthTextureWidth = depthTextureWidth;
        this.depthTextureHeight = depthTextureHeight;
        currentRead = ByteBuffer.allocateDirect(depthTextureWidth * depthTextureHeight * BYTES_PER_PIXEL);
        currentWrite = ByteBuffer.allocateDirect(depthTextureWidth * depthTextureHeight * BYTES_PER_PIXEL);
        currentWait = ByteBuffer.allocateDirect(depthTextureWidth * depthTextureHeight * BYTES_PER_PIXEL);
        //currentDepthMap = new float[depthTextureHeight * depthTextureWidth];
        frameBufferObject = new FrameBufferObject(depthTextureWidth, depthTextureHeight);
    }

    protected void readCurrentBuffers() {

        float[][] currentLidarDepths = lidarHelper.currentLidarDepths;
        ArrayList<LidarReading> readingsToPopulate = lidarHelper.bufferSpecificLidarRawReadings.get(depthBufferDirection.ordinal());

        for (LidarReading lidarReading : readingsToPopulate) {

            //Get the depth.
            float depth = getDepthAtPixelPercentages(lidarReading.getScreenXPercentage(), lidarReading.getScreenYPercentage());
            lidarReading.setDepth(depth);

            //Also populate the big array.
            currentLidarDepths[lidarReading.getBeamIndex()][lidarReading.getHorizontalIndex()] = depth;

        }

    }

    @Deprecated
    void readCurrentBuffer() {
        long startTime = System.currentTimeMillis();

        double nearPlane = lidarCamera.getProjectionMatrixHandler().getNearPlane();


        /*
        //Read the depth from the buffer.
        for (int y = 0; y < depthTextureHeight; y++) {
            for (int x = 0; x < depthTextureWidth; x++) {
                int pixelIndex = (y * depthTextureWidth) + x;
                int rgbStartIndex = pixelIndex * BYTES_PER_PIXEL;
                currentRead.position(rgbStartIndex);
                int redRaw = currentRead.get();
                int greenRaw = currentRead.get();
                int blueRaw = currentRead.get();
                int intBits = ((redRaw & 0xFF) << 16)
                        + ((greenRaw & 0xFF) << 8)
                        + (blueRaw & 0xFF);
                float lidarDistance = (float) ((((float) intBits) / MAX_DEPTH_INT_VALUE) * farPlane);
                currentDepthMap[pixelIndex] = lidarDistance;
            }
        }
        */

        //Populate the LiDAR readings:
        ArrayList<LidarReading> currentReadings = lidarHelper.getCurrentLidarReadings();

        if ((currentReadings.size() % LidarHelper.NUM_DIRECTIONS) != 0) {
            System.err.println("Number of readings isn't divisible by the number of directions!");
        }

        int numReadingsToPopulate = currentReadings.size() / LidarHelper.NUM_DIRECTIONS;
        int lidarStartIndex = depthBufferDirection.ordinal() * numReadingsToPopulate;

        for (int i = 0; i < numReadingsToPopulate; i++) {
            int readingIndex = lidarStartIndex + i;
            LidarReading lidarReading = currentReadings.get(readingIndex);
            int bufferDirection = lidarReading.getDepthBufferIndex();
            if (bufferDirection == depthBufferDirection.ordinal()) {
                lidarReading.setDepth(getDepthAtPixelPercentages(lidarReading.getScreenXPercentage(), lidarReading.getScreenYPercentage()));
            } else {
                System.err.println("Misaligned reading indices!");
            }
        }

        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
    }

    public void switchReadAndWaitBuffers() {
        ByteBuffer tempBuffer = currentRead;
        currentRead = currentWait;
        currentWait = tempBuffer;
    }

    public void readNewDepthData(Camera lidarCamera) {
        this.lidarCamera = lidarCamera;
        currentWrite.position(0);
        GL11.glReadPixels(0, 0, depthTextureWidth, depthTextureHeight, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, currentWrite);
    }

    public void switchWriteAndWaitBuffer() {
        ByteBuffer tempBuffer = currentWait;
        currentWait = currentWrite;
        currentWrite = tempBuffer;
    }

    public LidarHelper.DepthBufferDirection getDepthBufferDirection() {
        return depthBufferDirection;
    }

    public static Vector2f getRequiredDimensionsAtRevolutionSpeed(float revolutionsPerSecond) {
        float numPointsPerRevolution = LidarHelper.NUM_POINTS_PER_SECOND / revolutionsPerSecond;
        float horizontalPointsPerRevolution = numPointsPerRevolution / 32f;
        float requiredWidthAsAFloat = horizontalPointsPerRevolution / 4f;
        float requiredHeightAsAFloat = requiredWidthAsAFloat * (9f/16f);
        int requiredWidth = (int) Math.floor(requiredWidthAsAFloat);
        int requiredHeight = (int) Math.floor(requiredHeightAsAFloat); //Not really required.

        //OpenGL will crash with NON-Standard display sizes, set the size to be the closest larger value to desired.
        int bufferWidth = 320;
        while (bufferWidth < requiredWidth) {
            bufferWidth *= 2;
        }
        //Set the display to be 16:9
        int bufferHeight = (bufferWidth * 9) / 16;

        return new Vector2f(bufferWidth, bufferHeight);
    }

    public FrameBufferObject getFrameBuffer() {
        return frameBufferObject;
    }
}
