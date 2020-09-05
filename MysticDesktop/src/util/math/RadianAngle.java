package util.math;

/**
 * Created by p3te on 26/10/16.
 */
public class RadianAngle {

    public static boolean withinRange(double value, double min, double firstValueOver) {
        return ((value >= min) && (value < firstValueOver));
    }

    public static double normalizeAngle(double angle) {
        angle = angle % (Math.PI * 2.0);
        if (angle < 0.0) {
            angle = (Math.PI * 2.0) + angle;
        }
        return angle;
    }

    public static boolean angleWithinRange(double angle, double minAngle, double maxAngle) {
        angle = normalizeAngle(angle);
        minAngle = normalizeAngle(minAngle);
        maxAngle = normalizeAngle(maxAngle);

        if (maxAngle < minAngle) {
            //max angle was > 360 degrees, and min angle wasn't.

            if ((angle <= maxAngle) || (angle >= minAngle)) {
                return true;
            } else {
                return false;
            }

        } else {

            if ((angle <= maxAngle) && (angle >= minAngle)) {
                return true;
            } else {
                return false;
            }

        }

    }

}
