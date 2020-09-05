package gebd.games.boat.lidar;

import gebd.Render;
import gebd.camera.Camera;
import gebd.camera.implementation.NoClipCamera;
import util.math.RadianAngle;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import java.util.ArrayList;

/**
 * Created by P3TE on 8/08/2016.
 */
public class LidarHelper {








    /**
     * The (desired) angles of the Velodyne. These are specified in degrees
     * and should be converted to radians where applicable.
     */
    public static final float[] VELODYNE_VERTICAL_ANGLES = {
            -30.67f,
            -9.33f,
            -29.33f,
            -8.00f,
            -28.00f,
            -6.66f,
            -26.66f,
            -5.33f,
            -25.33f,
            -4.00f,
            -24.00f,
            -2.67f,
            -22.67f,
            -1.33f,
            -21.33f,
            0.00f,
            -20.00f,
            1.33f,
            -18.67f,
            2.67f,
            -17.33f,
            4.00f,
            -16.00f,
            5.33f,
            -14.67f,
            6.67f,
            -13.33f,
            8.00f,
            -12.00f,
            9.33f,
            -10.67f,
            10.67f
    };

    public enum LidarRotationSpeed {
        Hz5(5f, 1, 90f, 640, 640),
        Hz10(10f, 2, 90f, Render.WIDTH / 2, Render.WIDTH / 2),
        Hz20(20f, 4, 90f, 640, 640);

        private float revolutionsPerSecond;
        private int buffersUpdatedPerFrame;
        private float fieldOfView;
        private int depthBufferWidth;
        private int depthBufferHeight;

        LidarRotationSpeed(float revolutionsPerSecond, int buffersUpdatedPerFrame, float FOV, int depthBufferWidth, int depthBufferHeight) {
            this.revolutionsPerSecond = revolutionsPerSecond;
            this.buffersUpdatedPerFrame = buffersUpdatedPerFrame;
            this.fieldOfView = FOV;
            this.depthBufferWidth = depthBufferWidth;
            this.depthBufferHeight = depthBufferHeight;
        }

        public float getRevolutionsPerSecond() {
            return revolutionsPerSecond;
        }

        public int getBuffersUpdatedPerFrame() {
            return buffersUpdatedPerFrame;
        }

        public float getFieldOfView() {
            return fieldOfView;
        }

        public Vector2f getBufferSize(){
            return new Vector2f(depthBufferWidth, depthBufferHeight);
        }

        public float getAspectRatio(){
            return (((float) depthBufferWidth) / ((float) depthBufferHeight));
        }
    }

    public enum DepthBufferDirection {
        FORWARD(0),
        LEFT(Math.PI / 2.0),
        BACKWARD(Math.PI),
        RIGHT(Math.PI * (3.0 / 2.0));

        private double thetaAngleOffset;

        DepthBufferDirection(double thetaAngleOffset) {
            this.thetaAngleOffset = thetaAngleOffset;
        }

        public double getThetaAngleOffset() {
            return thetaAngleOffset;
        }

    }


    private static Camera camera;

    private LidarCalculationHandler[] lidarCalculationHandlers = new LidarCalculationHandler[LidarHelper.DepthBufferDirection.values().length];
    public static final int NUM_DIRECTIONS = LidarHelper.DepthBufferDirection.values().length;

    public static final int NUM_POINTS_PER_SECOND = 700000;

    public static final Vector3f LIDAR_POSITION_OFFSET = new Vector3f(0f, 1.16f - 0.05f, 0f);

    private float numRevolutionsPerSecond = 20;
    public static final int NUM_BEAMS = VELODYNE_VERTICAL_ANGLES.length;
//    public static final float MAX_ANGLE_UP = (float) Math.toRadians(10.67);
//    public static final float MAX_ANGLE_DOWN = (float) Math.toRadians(-30.67);
//    public static final float PHI_OFFSET = ((MAX_ANGLE_DOWN + MAX_ANGLE_UP) / 2f);
    public static final float MAX_ANGLE_UP = (float) Math.toRadians(10.67);
    public static final float MAX_ANGLE_DOWN = (float) Math.toRadians(-30.67);
    public static final float PHI_OFFSET = 0f;

    private static Vector2f depthTextureSize;

    private LidarRotationSpeed lidarRotationSpeed = LidarRotationSpeed.Hz5;

    public LidarHelper() {
        //Nothing done here anymore.
    }

    private ArrayList<LidarCalculationThread> lidarCalculationThreads = new ArrayList<>(4);

    private int numPointsPerCycle;
    private int numPointsPerDepthBuffer;
    private int numHorizontalPointsPerBuffer;
    private DirectionalLidarPoints directionalLidarPoints;

    private static ArrayList<LidarReading> currentLidarReadings;


    //Package private so it can be accessed on the LidarCalculationThread. ;)
    static LidarReading[][] currentLidarRawReadings;
    float[][] currentLidarDepths;
    float[] horizontalAngles;
    ArrayList<ArrayList<LidarReading>> bufferSpecificLidarRawReadings;

    public static LidarReading[][] getCurrentLidarRawReadings() {
        return currentLidarRawReadings;
    }

    public float[][] getCurrentLidarDepths() {
        return currentLidarDepths;
    }

    public float[] getHorizontalAngles() {
        return horizontalAngles;
    }

    public static final int NUM_HORIZONTAL_READINGS = 2172;
    private static final int numDirections = DepthBufferDirection.values().length;
    private static final int horizontalReadingsPerBuffer = NUM_HORIZONTAL_READINGS / numDirections;
    private static final double DIST_BETWEEN_HORIZONTAL_ANGLES = (Math.PI / 2.0) / ((double) horizontalReadingsPerBuffer);

    private LidarReading getReadingFromAbsoulteRotation(double theta, double phi) {

        LidarReading lidarReading = new LidarReading();

        double relativePhi = phi - PHI_OFFSET;

        DepthBufferDirection bufferDirection = null;

        for (DepthBufferDirection depthBufferDirection : DepthBufferDirection.values()) {

            //Determine the range of angles covered by the LiDAR camera.
            double minPossibleTheta = depthBufferDirection.getThetaAngleOffset() + (Math.PI / 4.0);
            double maxPossibleTheta = minPossibleTheta + (Math.PI / 2.0);

            if (RadianAngle.angleWithinRange(theta, minPossibleTheta, maxPossibleTheta)) {
                //Found the buffer this point should belong to.
                bufferDirection = depthBufferDirection;

                //Determine the relative theta.
                double relativeTheta = RadianAngle.normalizeAngle(theta - depthBufferDirection.getThetaAngleOffset());

                //Determine the screen X,Y percentage.
                Vector3f requestedRayInto = calcRayFromThetaPhi(relativeTheta, relativePhi);
                Vector2f screenCoords = camera.getScreenCoordsFromRay(requestedRayInto, depthTextureSize);
                float pixelXPercentage = screenCoords.x / depthTextureSize.x;
                if ((pixelXPercentage >= 1.0) && (pixelXPercentage < 1.00001)) {
                    pixelXPercentage = 0.9999999f;
                }
                float pixelYPercentage = screenCoords.y / depthTextureSize.y;

                //populate pixel percentages.
                lidarReading.setScreenXPercentage(pixelXPercentage);
                lidarReading.setScreenYPercentage(pixelYPercentage);

                //Ensure that they are in range.
                if (!RadianAngle.withinRange(pixelXPercentage, 0.0, 1.0)) {
                    throw new RuntimeException("Pixel percentage out of range! (x,y) = (" + pixelXPercentage + "," + pixelYPercentage + ")");
                }
                if (!RadianAngle.withinRange(pixelYPercentage, 0.0, 1.0)) {
                    throw new RuntimeException("Pixel percentage out of range! (x,y) = (" + pixelXPercentage + "," + pixelYPercentage + ")");
                }

                /*
                 * We've given it the phi and theta.
                 * However, being a depth buffer, it only works in pixels.
                 * So now let's determine what the real angle I'm getting back is.
                 */

                //Pixelate the positions.
                int pixelRoundedScreenCoordX = (int) (screenCoords.x);
                int pixelRoundedScreenCoordY = (int) (screenCoords.y);
                Vector2f pixelRoundedScreenCoords = new Vector2f(pixelRoundedScreenCoordX, pixelRoundedScreenCoordY);

                //get the relative theta & phi from the pixel coords.
                Vector3f absoluteRayIntoScreen = camera.getRayFromScreenCoords(pixelRoundedScreenCoords, depthTextureSize);
                Vector2f pixelRelativeThetaPhi = calThetaPhi(absoluteRayIntoScreen);

                //Translate back to absolute theta and phi.
                double absoluteTheta = pixelRelativeThetaPhi.x + depthBufferDirection.getThetaAngleOffset();
                double absolutePhi = pixelRelativeThetaPhi.y + PHI_OFFSET;

                //Set the true theta & phi values.
                lidarReading.setTheta(absoluteTheta);
                lidarReading.setPhi(absolutePhi);

                break;
            }

        }

        if (bufferDirection == null) {
            throw new RuntimeException("bufferDirection was null for theta: " + theta);
        }

        //Set the depth buffer index.
        lidarReading.setDepthBufferIndex(bufferDirection.ordinal());

        return lidarReading;

    }


    private void setupRawReadings(){

        int totalReadingCount = NUM_BEAMS * NUM_HORIZONTAL_READINGS;
        currentLidarRawReadings = new LidarReading[NUM_BEAMS][NUM_HORIZONTAL_READINGS];
        currentLidarDepths = new float[NUM_BEAMS][NUM_HORIZONTAL_READINGS];
        horizontalAngles = new float[NUM_HORIZONTAL_READINGS];
        double[] horizontalAngleTotals = new double[NUM_HORIZONTAL_READINGS];

        bufferSpecificLidarRawReadings = new ArrayList<>(numDirections);

        //Add a reading arraylist for each direction.
        for (DepthBufferDirection direction : DepthBufferDirection.values()) {
            bufferSpecificLidarRawReadings.add(new ArrayList<LidarReading>());
        }

        for (int beamNo = 0; beamNo < NUM_BEAMS; beamNo++) {

            double phi = Math.toRadians(VELODYNE_VERTICAL_ANGLES[beamNo]);

            for (int horizontalIndex = 0; horizontalIndex < NUM_HORIZONTAL_READINGS; horizontalIndex++) {

                double thetaIndexAsDouble = ((double) horizontalIndex) + 0.5;
                double requestedAbsoluteTheta = (Math.PI * 2.0) - (thetaIndexAsDouble * DIST_BETWEEN_HORIZONTAL_ANGLES);
                LidarReading lidarReading = getReadingFromAbsoulteRotation(requestedAbsoluteTheta, phi);

                //Set the indices of the beam (beamNo, and horizontal index).
                lidarReading.setBeamIndex(beamNo);
                lidarReading.setHorizontalIndex(horizontalIndex);

                //Add the new reading to the correct camera.
                bufferSpecificLidarRawReadings.get(lidarReading.getDepthBufferIndex()).add(lidarReading);
                currentLidarRawReadings[beamNo][horizontalIndex] = lidarReading;

                //Update the running total for the combined horizontal theta.
                horizontalAngleTotals[horizontalIndex] += lidarReading.getTheta();

            }

        }

        //Calculate the average theta for each horizontal index.
        for (int horizontalIndex = 0; horizontalIndex < NUM_HORIZONTAL_READINGS; horizontalIndex++) {

            double simAngle = (horizontalAngleTotals[horizontalIndex] / ((double) NUM_BEAMS));
            double exportedAngle = Math.toDegrees(RadianAngle.normalizeAngle((Math.PI - simAngle)));
            horizontalAngles[horizontalIndex] = (float) exportedAngle;

            //horizontalAngles[horizontalIndex] = (float) (horizontalAngleTotals[horizontalIndex] / ((double) NUM_BEAMS));
        }

    }





    public LidarCalculationHandler getLidarCalculationHandler(int directionOrdinal) {
        return lidarCalculationHandlers[directionOrdinal];
    }

    public float getNumRevolutionsPerSecond() {
        return numRevolutionsPerSecond;
    }

    public int getNumBeams() {
        return NUM_BEAMS;
    }

    public int getNumPointsPerCycle() {
        return numPointsPerCycle;
    }

    public int getNumPointsPerDepthBuffer() {
        return numPointsPerDepthBuffer;
    }

    public int getNumHorizontalPointsPerBuffer() {
        return numHorizontalPointsPerBuffer;
    }

    public DirectionalLidarPoints getDirectionalLidarPoints() {
        return directionalLidarPoints;
    }

    public static ArrayList<LidarReading> getCurrentLidarReadings() {
        return currentLidarReadings;
    }

    public ArrayList<ArrayList<LidarReading>> getBufferSpecificLidarRawReadings() {
        return bufferSpecificLidarRawReadings;
    }

    public LidarRotationSpeed getLidarRotationSpeed() {
        return lidarRotationSpeed;
    }

    public void setLidarRotationSpeed(LidarRotationSpeed lidarRotationSpeed) {
        this.lidarRotationSpeed = lidarRotationSpeed;
        lidarCalculationHandlers = LidarCalculationThread.generateLidarAtHz(lidarRotationSpeed.getRevolutionsPerSecond(), this);

        lidarCalculationThreads = new ArrayList<>(LidarHelper.NUM_DIRECTIONS);
        for (DepthBufferDirection depthBufferDirection : DepthBufferDirection.values()) {
            LidarCalculationThread newCalculationThread = new LidarCalculationThread(this, depthBufferDirection);
            lidarCalculationThreads.add(newCalculationThread);
        }

        //Setup the LiDAR Readings.
        this.depthTextureSize = lidarRotationSpeed.getBufferSize();
        float cameraTheta = (float) (Math.PI / 2.0);
        float cameraPhi = 0f;
        this.camera = new NoClipCamera(new Vector3f(), cameraTheta, cameraPhi);
        this.camera.getProjectionMatrixHandler().setAspectRatio(lidarRotationSpeed.getAspectRatio());
        this.camera.getProjectionMatrixHandler().setFieldOfView(lidarRotationSpeed.getFieldOfView());
        this.camera.calculateRotationMatrix();
        //calculateAngles();
        setupRawReadings();

        //This has to be last.
        this.currentLidarReadings = calculateAngles(lidarRotationSpeed);
    }

    @Deprecated
    private void calculateAngles() {
        lidarCalculationThreads = new ArrayList<>(LidarHelper.NUM_DIRECTIONS);
        for (DepthBufferDirection depthBufferDirection : DepthBufferDirection.values()) {
            LidarCalculationThread newCalculationThread = new LidarCalculationThread(this, depthBufferDirection);
            lidarCalculationThreads.add(newCalculationThread);
        }

        numPointsPerCycle = (int) Math.floor(NUM_POINTS_PER_SECOND / numRevolutionsPerSecond);
        numPointsPerDepthBuffer = (int) Math.floor(numPointsPerCycle / 4.0);
        numHorizontalPointsPerBuffer = numPointsPerDepthBuffer / NUM_BEAMS;

        currentLidarReadings = new ArrayList<>(numPointsPerCycle);
        for (int i = 0; i < numPointsPerCycle; i++) {
            currentLidarReadings.add(new LidarReading());
        }

        double angleRangePerBuffer = Math.PI / 2.0;
        double horizontalAngleRange = angleRangePerBuffer / ((double) numHorizontalPointsPerBuffer);
        double angleStartOffset = (horizontalAngleRange / 2.0);

        double verticalAngleBetweenBeams = (MAX_ANGLE_UP - MAX_ANGLE_DOWN) / ((double) (NUM_BEAMS - 1));

        directionalLidarPoints = new DirectionalLidarPoints(NUM_BEAMS, numHorizontalPointsPerBuffer);

        double theta = 0;
        for (int horizontalPoint = 0; horizontalPoint < numHorizontalPointsPerBuffer; horizontalPoint++) {
            double relativeTheta = (-angleRangePerBuffer / 2.0) + angleStartOffset + (horizontalPoint * horizontalAngleRange);
            theta = relativeTheta + (Math.PI / 2.0);

            for (int verticalPoint = 0; verticalPoint < NUM_BEAMS; verticalPoint++) {
                //double phi = MAX_ANGLE_DOWN + (verticalAngleBetweenBeams * verticalPoint);
                double phi = Math.toRadians(VELODYNE_VERTICAL_ANGLES[verticalPoint]);
                double relativePhi = (float) phi - PHI_OFFSET;
                Vector3f rayInto = calcRayFromThetaPhi(theta, relativePhi);
                Vector2f screenCoords = camera.getScreenCoordsFromRay(rayInto, depthTextureSize);
                float pixelXPercentage = screenCoords.x / depthTextureSize.x;
                float pixelYPercentage = screenCoords.y / depthTextureSize.y;
                LidarPointInfo lidarPointInfo = new LidarPointInfo(relativeTheta, relativePhi, pixelXPercentage, pixelYPercentage);
                directionalLidarPoints.pointInfos.get(verticalPoint).set(horizontalPoint, lidarPointInfo);

                for (int angleOffset = 0; angleOffset < 4; angleOffset++) {
                    int indexOffset = angleOffset * numPointsPerDepthBuffer;
                    int pointIndex = verticalPoint + (horizontalPoint * NUM_BEAMS) + indexOffset;
                    LidarReading lidarReading = currentLidarReadings.get(pointIndex);
                    //lidarReading.setScreenX(Math.round(screenCoords.x));
                    //lidarReading.setScreenY(Math.round(screenCoords.y));

                    if ((screenCoords.x < 0) || (screenCoords.x >=depthTextureSize.x)) {
                        System.err.println("Screen coords out of bounds in x!");
                    }

                    if ((screenCoords.y < 0) || (screenCoords.y >=depthTextureSize.y)) {
                        System.err.println("Screen coords out of bounds in y!");
                    }

                    /*
                     * TODO - IMPORTANT - Think about whether you want:
                      * The pixel chosen to be the closest to the velodyne reading
                      * OR
                      * The angle to be exactly what the pixel is giving.
                     */
                    lidarReading.setTheta(theta + ((Math.PI / 2.0) * angleOffset));
                    lidarReading.setPhi(phi);
                }
            }
        }
    }

    private ArrayList<LidarReading> calculateAngles(LidarRotationSpeed rotationSpeed) {

        //Initialize the buffer dimensions.
        Vector2f requiredBufferDimensions = LidarCalculationHandler.getRequiredDimensionsAtRevolutionSpeed(rotationSpeed.revolutionsPerSecond);
        int resultsSize = (int) (requiredBufferDimensions.x * 4 * NUM_BEAMS);
        ArrayList<LidarReading> result = new ArrayList<>(resultsSize);

        int numHorizontalBeamsPerBuffer = (int) requiredBufferDimensions.x;

        int numBufferDirections = DepthBufferDirection.values().length;
        for (int bufferDirection = 0; bufferDirection < numBufferDirections; bufferDirection++) {

            DepthBufferDirection depthBufferDirection = DepthBufferDirection.values()[bufferDirection];
            //
            for (int verticalBeam = 0; verticalBeam < NUM_BEAMS; verticalBeam++) {

                float verticalAngleRange = (MAX_ANGLE_UP - MAX_ANGLE_DOWN);
                float verticalBeamPercentage = ((float) verticalBeam) / ((float) (NUM_BEAMS - 1)); //Between 0.0 and 1.0 (Inclusive).
                float absolutePhi = (verticalAngleRange * verticalBeamPercentage) + MAX_ANGLE_DOWN;
                float relativePhi = absolutePhi - PHI_OFFSET;

                for (int horizontalBeam = 0; horizontalBeam < numHorizontalBeamsPerBuffer; horizontalBeam++) {

                    float minRequiredAngle = (float) (-(Math.PI / 4.0));
                    float maxRequiredAngle = (float) (+(Math.PI / 4.0));
                    float totalThetaRange = maxRequiredAngle - minRequiredAngle;
                    float thetaIndexCountFloat = (float) numHorizontalBeamsPerBuffer;

                    float percentageOfThetaRange = (((float) horizontalBeam) / thetaIndexCountFloat) + (1.0f / (thetaIndexCountFloat * 2.0f));
                    float relativeTheta = (percentageOfThetaRange * totalThetaRange) + minRequiredAngle;
                    float absoluteTheta = (float) (depthBufferDirection.getThetaAngleOffset() + relativeTheta);
                    float translateTheta = (float) (relativeTheta + (Math.PI / 2.0));

                    Vector2f pixelPercentage = getPixelPercentageFromAngle(translateTheta, relativePhi);
                    LidarReading newLidarReading = new LidarReading();

                    //Setup the new LiDAR reading.
                    newLidarReading.setDepthBufferIndex(bufferDirection);
                    newLidarReading.setScreenXPercentage(pixelPercentage.x);
                    newLidarReading.setScreenYPercentage(pixelPercentage.y);
                    newLidarReading.setTheta(absoluteTheta);
                    newLidarReading.setPhi(absolutePhi);
                    result.add(newLidarReading);

                    if ((pixelPercentage.x < 0) || (pixelPercentage.x >= 1.0f)) {
                        System.err.println("Screen coords out of bounds in x!");
                    }
                    if ((pixelPercentage.y < 0) || (pixelPercentage.y >= 1.0f)) {
                        System.err.println("Screen coords out of bounds in y!");
                    }
                    
                }


            }

        }

        return result;
    }

    public static Vector2f getPixelPercentageFromAngle(double theta, double phi) {
        //double phi = MAX_ANGLE_DOWN + (verticalAngleBetweenBeams * verticalPoint);
        //double relativePhi = (float) phi - PHI_OFFSET;
        Vector3f rayInto = calcRayFromThetaPhi(theta, phi);
        Vector2f screenCoords = camera.getScreenCoordsFromRay(rayInto, depthTextureSize);
        float pixelXPercentage = screenCoords.x / depthTextureSize.x;
        float pixelYPercentage = screenCoords.y / depthTextureSize.y;
        LidarPointInfo lidarPointInfo = new LidarPointInfo(theta, phi, pixelXPercentage, pixelYPercentage);
        return new Vector2f(pixelXPercentage, pixelYPercentage);
    }

    private static Vector3f calcRayFromThetaPhi(double theta, double phi) {
        double yDot = Math.sin(phi);
        double rDot = Math.cos(phi);
        double zDot = -rDot * Math.sin(theta);
        double xDot = rDot * Math.cos(theta);
        return new Vector3f((float) xDot, (float) yDot, (float) zDot);
    }

    private Vector2f calThetaPhi(Vector3f xyz) {
        double rDot = Math.sqrt((xyz.x * xyz.x) + (xyz.z * xyz.z));
        double theta = Math.atan2(-xyz.z, xyz.x);
        double phi = Math.atan2(xyz.y, rDot);
        return new Vector2f((float) theta, (float) phi);
    }

    public void addLidarReadings(Camera lidarCamera, LidarCalculationHandler newLidarCalculationHandler) {
        long startTime = System.currentTimeMillis();
        DepthBufferDirection depthBufferDirection = newLidarCalculationHandler.getDepthBufferDirection();
        lidarCalculationThreads.get(depthBufferDirection.ordinal()).addBufferInformation(lidarCamera, newLidarCalculationHandler);
        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
    }

    private class DirectionalLidarPoints {
        private ArrayList<ArrayList<LidarPointInfo>> pointInfos;

        public DirectionalLidarPoints(int numBeams, int numHorizontalPointsPerBuffer) {
            pointInfos = new ArrayList<>(numBeams);
            for (int i = 0; i < numBeams; i++) {
                ArrayList<LidarPointInfo> innerArrayList = new ArrayList<LidarPointInfo>(numHorizontalPointsPerBuffer);
                for (int j = 0; j < numHorizontalPointsPerBuffer; j++) {
                    innerArrayList.add(null);
                }
                pointInfos.add(innerArrayList);
            }
        }



    }

    private static class LidarPointInfo {
        private float theta;
        private float phi;
        private float pixelXPercentage;
        private float pixelYPercentage;

        public LidarPointInfo(double theta, double phi, float pixelXPercentage, float pixelYPercentage) {
            this.theta = (float) theta;
            this.phi = (float) phi;
            this.pixelXPercentage = pixelXPercentage;
            this.pixelYPercentage = pixelYPercentage;
        }
    }

}
