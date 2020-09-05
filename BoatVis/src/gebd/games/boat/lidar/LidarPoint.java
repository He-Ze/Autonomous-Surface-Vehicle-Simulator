package gebd.games.boat.lidar;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;


public class LidarPoint {

    /*点的极坐标*/
    static  float depth=0;
    static  double theta=0;
    static  double phi=0;

    /*点的xyz坐标*/
    static  float pointX=0;
    static  float pointY=0;
    static  float pointZ=0;

    /*提供角度的构造函数*/
    public LidarPoint(double theta_, double phi_){

        Vector2f xyp = LidarHelper.getPixelPercentageFromAngle(theta_, phi_);
        float q = xyp.x;
        float p = xyp.y;
        float depth_ = LidarCalculationHandler.getDepthAtPixelPercentages(q, p);

        depth = depth_;
        theta = theta_;
        phi = phi_;
        Vector3f xyz = calcXYZFromThetaPhi(theta_, phi_);
        pointX = depth_ * xyz.x;
        pointY = depth_ * xyz.y;
        pointZ = depth_ * xyz.z;
    }
    private static Vector3f calcXYZFromThetaPhi(double theta, double phi) {
        double yDot = Math.sin(phi);
        double rDot = Math.cos(phi);
        double zDot = -rDot * Math.sin(theta);
        double xDot = rDot * Math.cos(theta);
        return new Vector3f((float) xDot, (float) yDot, (float) zDot);
    }

    /*提供xy的像素比的构造函数*/
    /*public LidarPoint(float q, float p){

        //Vector2f xyp = LidarHelper.getPixelPercentageFromAngle(theta_, phi_);
        //float q = xyp.x;
        //float p = xyp.y;
        float depth_ = LidarCalculationHandler.getDepthAtPixelPercentages(q, p);

        depth = depth_;
        //theta = theta_;
        //phi = phi_;
        Vector3f xyz = calcXYZFromThetaPhi(theta_, phi_);
        pointX = depth_ * xyz.x;
        pointY = depth_ * xyz.y;
        pointZ = depth_ * xyz.z;
    }*/

    /*通过x，y，z计算theta，phi和depth*/
    /*private Vector2f calThetaPhiFromXYZ(Vector3f xyz) {
        double rDot = Math.sqrt((xyz.x * xyz.x) + (xyz.z * xyz.z));
        double theta = Math.atan2(-xyz.z, xyz.x);
        double phi = Math.atan2(xyz.y, rDot);
        return new Vector2f((float) theta, (float) phi);
    }*/

    public static String getXYZ() {return new Vector3f((float) pointX, (float) pointY, (float) pointZ).toString();}
    public static String getDTP() {return new Vector3f((float) depth, (float) theta, (float) phi).toString();}

}
