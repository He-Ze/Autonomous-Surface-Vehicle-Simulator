package physics.util;

import javax.vecmath.*;

/**
 * Created by CaptainPete on 9/10/2016.
 */
public class Plane {

    public static AxisAngle4f rotatePlaneSoUpIsNowOtherThing(Vector3f planeNormal, Vector3f desiredNormal) {

        Vector3f normalizedPlaneNormal = new Vector3f(planeNormal);
        normalizedPlaneNormal.normalize();

        Vector3f normalizedDesiredNormal = new Vector3f(desiredNormal);
        normalizedDesiredNormal.normalize();

        Vector3f crossProduct = new Vector3f();
        crossProduct.cross(normalizedPlaneNormal, normalizedDesiredNormal);
        float angle = (float) -Math.acos(normalizedPlaneNormal.dot(normalizedDesiredNormal));

        return new AxisAngle4f(crossProduct.x, crossProduct.y, crossProduct.z, angle);
    }

    public static float determineSmallestDistanceFromPointToLine(Vector3f pointOnLine, Vector3f directionOfLine, Vector3f point) {
        Vector3f p1 = new Vector3f(pointOnLine);
        Vector3f p2 = new Vector3f(p1);
        p2.add(directionOfLine);

        Vector3f p2p1 = new Vector3f(p2);
        p2p1.sub(p1);

        Vector3f p2pc = new Vector3f(p2);
        p2pc.sub(point);

        Vector3f crossp2p1Withp2pc = new Vector3f();
        crossp2p1Withp2pc.cross(p2p1, p2pc);

        float distance = crossp2p1Withp2pc.length() / p2p1.length();
        return distance;
    }

    public static Vector3f determineClosestPointOnLineToAPoint2(Vector3f pointOnLine, Vector3f directionOfLine, Vector3f point) {

        Vector3f p0 = new Vector3f(pointOnLine);

        Vector3f b = new Vector3f(point);
        b.sub(pointOnLine);

        float lengthOfA = directionOfLine.length();
        float ditanceFromLinePointToClosestPoint = directionOfLine.dot(b) / lengthOfA;

        Vector3f pointOnLineClosestToPoint = new Vector3f(directionOfLine);
        pointOnLineClosestToPoint.scale(ditanceFromLinePointToClosestPoint / lengthOfA);
        pointOnLineClosestToPoint.add(pointOnLine);

        return pointOnLineClosestToPoint;
    }

    public static Vector3f determineClosestPointOnLineToAPoint(Vector3f pointOnLine, Vector3f directionOfLine, Vector3f point) {

        Vector3f p1 = new Vector3f(pointOnLine);
        Vector3f p2 = new Vector3f(p1);
        p2.add(directionOfLine);

        Vector3f p1p2 = new Vector3f(p2);
        p1p2.sub(p1);

        Vector3f p1pc = new Vector3f(point);
        p1pc.sub(p1);

        if (p1pc.length() < 0.001f) {
            //This isn't going to work if the point is on the line.
            return point;
        }

        Vector3f dot_p1p2Withp1pc = new Vector3f(p1p2);
        dot_p1p2Withp1pc.dot(p1pc);

        Vector3f closestPoint = new Vector3f();
        closestPoint.scale(p1p2.length(), dot_p1p2Withp1pc);

        return closestPoint;
    }



    /**
     * Given that the planes are in the form:
     *
     * @param plane1 - Plane 1: (ax + by + cz + d = 0)
     * @param plane2 - Plane 2: (ax + by + cz + d = 0)
     * @return - A 3D point that lies on the plane.
     */
    public static Vector3f getPointOnIntersectionOfTwoPlanes(Vector4f plane1, Vector4f plane2) {
        float d = plane1.w;
        float h = plane2.w;

        float a = plane1.x;
        float b = plane1.y;
        float c = plane1.z;

        float e = plane2.x;
        float f = plane2.y;
        float g = plane2.z;

        if ((e * e) < (g * g)) {
            //Let x = 0.
            float x = 0;
            float z = (h - ((f * d) / b)) / (g - ((f * c) / b));
            float y = ((d - (c * z)) / b);

            return new Vector3f(x, y, z);
        } else {
            //Let z = 0.
            float z = 0;
            float x = (h - ((f * d) / b)) / (e - ((a * f) / b));
            float y = ((d - (a * x)) / b);

            return new Vector3f(x, y, z);
        }
    }

    public static Vector4f getPlaneAtPosition(Vector3f position1, Vector3f position2, Vector3f position3) {
        Matrix3f knownPositions = new Matrix3f();
        knownPositions.m00 = position1.x;
        knownPositions.m10 = position1.y;
        knownPositions.m20 = position1.z;

        knownPositions.m01 = position2.x;
        knownPositions.m11 = position2.y;
        knownPositions.m21 = position2.z;

        knownPositions.m02 = position3.x;
        knownPositions.m12 = position3.y;
        knownPositions.m22 = position3.z;

        knownPositions.invert();

        float d = -1f;
        Vector3f abc = new Vector3f(-d, -d, -d);
        knownPositions.transform(abc);

        return new Vector4f(abc.x, abc.y, abc.z, d);
    }

    public static Vector4f getPlaneAtPosition2(Vector3f position1, Vector3f position2, Vector3f position3) {

        Vector3f p1p2 = new Vector3f(position2);
        p1p2.sub(position1);

        Vector3f p1p3 = new Vector3f(position3);
        p1p3.sub(position1);

        Vector3f planeNormal = new Vector3f();
        planeNormal.cross(p1p2, p1p3);

        //ax + by + cz - d = 0
        float d = planeNormal.dot(position1);

        return new Vector4f(planeNormal.x, planeNormal.y, planeNormal.z, d);
    }

    public static boolean isPlaneValidFromPoints(Vector3f position1, Vector3f position2, Vector3f position3) {
        double angleBetweenTwoVectors = getAngleBetweenThree3dPoints(position1, position2, position3);
        if (angleBetweenTwoVectors < 0.001) {
            return false;
        }
        return true;
    }

    public static float getAngleBetweenTwoVector2fs(Vector2f vector1, Vector2f vector2) {
        float dotProduct = vector1.dot(vector2);
        float cosOfAngle = dotProduct / (vector1.length() * vector2.length());
        if (cosOfAngle >= 1.0) {
            //Will result in NAN if over 1.0! (NOT GOOD!)
            return 0f;
        }
        return (float) Math.acos(cosOfAngle);
    }

    public static float getAngleBetweenThree2dPoints(Vector2f point1, Vector2f point2, Vector2f point3) {
        Vector2f vector1 = new Vector2f(point2);
        vector1.sub(point1);

        Vector2f vector2 = new Vector2f(point3);
        vector2.sub(point1);

        return getAngleBetweenTwoVector2fs(vector1, vector2);
    }

    public static float getAngleBetweenTwoVector3fs(Vector3f vector1, Vector3f vector2) {
        float dotProduct = vector1.dot(vector2);
        float cosOfAngle = dotProduct / (vector1.length() * vector2.length());
        if (cosOfAngle >= 1.0) {
            //Will result in NAN if over 1.0! (NOT GOOD!)
            return 0f;
        }
        return (float) Math.acos(cosOfAngle);
    }

    public static float getAngleBetweenThree3dPoints(Vector3f point1, Vector3f point2, Vector3f point3) {
        Vector3f vector1 = new Vector3f(point2);
        vector1.sub(point1);

        Vector3f vector2 = new Vector3f(point3);
        vector2.sub(point1);

        return getAngleBetweenTwoVector3fs(vector1, vector2);
    }

}
