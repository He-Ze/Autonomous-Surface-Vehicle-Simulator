package util.math;

/**
 * Created by p3te on 26/10/16.
 */
public class DegreeAngle {

    public static double normalizeAngle(double angle) {
        angle = angle % (360.0);
        if (angle < 0.0) {
            angle = (360.0) + angle;
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
