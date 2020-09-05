package gebd.games.boat.lidar;

/**
 * Created by CaptainPete on 8/9/2016.
 */
public class LidarReading {

    //Used for determining where to index the depth from.
    private static int depthBufferIndex;
    private float screenXPercentage;
    private float screenYPercentage;

    private double theta;
    private double phi;
    public static double depth; //This will actually just be floating point precision.

    int beamIndex;
    int horizontalIndex;

    public LidarReading() {
        theta = 0;
        phi = 0;
        depth = 0;
    }

    public double getTheta() {
        return theta;
    }

    public double getPhi() {
        return phi;
    }

    public static double getDepth() {
        return depth;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public void setPhi(double phi) {
        this.phi = phi;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public static int getDepthBufferIndex() {
        return depthBufferIndex;
    }

    public void setDepthBufferIndex(int depthBufferIndex) {
        this.depthBufferIndex = depthBufferIndex;
    }

    public float getScreenXPercentage() {
        return screenXPercentage;
    }

    public void setScreenXPercentage(float screenXPercentage) {
        this.screenXPercentage = screenXPercentage;
    }

    public float getScreenYPercentage() {
        return screenYPercentage;
    }

    public void setScreenYPercentage(float screenYPercentage) {
        this.screenYPercentage = screenYPercentage;
    }

    public int getBeamIndex() {
        return beamIndex;
    }

    public void setBeamIndex(int beamIndex) {
        this.beamIndex = beamIndex;
    }

    public int getHorizontalIndex() {
        return horizontalIndex;
    }

    public void setHorizontalIndex(int horizontalIndex) {
        this.horizontalIndex = horizontalIndex;
    }
}
