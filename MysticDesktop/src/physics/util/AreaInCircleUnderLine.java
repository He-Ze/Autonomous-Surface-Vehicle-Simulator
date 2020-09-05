package physics.util;

import blindmystics.util.vector.Matrix2f;

import javax.vecmath.Vector2f;

/**
 * Created by CaptainPete on 8/31/2016.
 */
public class AreaInCircleUnderLine {


    public static float determineArea(float radius, float distanceToIntersectingLine, boolean aboveTheLine) {

        if (aboveTheLine) {
            if (distanceToIntersectingLine > radius) {
                //The line is above the circle.
                return (float) (Math.PI * radius * radius);
            } else {
                float totalArea = (float) (Math.PI * radius * radius);
                totalArea -= determineAreaBetweenArcAndLine(radius, distanceToIntersectingLine);
                return totalArea;
            }
        } else {
            if (distanceToIntersectingLine > radius) {
                //The line is below the circle.
                return 0f;
            } else {
                return determineAreaBetweenArcAndLine(radius, distanceToIntersectingLine);
            }
        }
    }


    /**
     * Determines the area in a circle under a given line.
     * @param radius - The radius of the circle.
     * @param centerOfCircle - The position of the center of the circle.
     * @param linePointOnLeft - The first point making up the line.
     * @param linePointOnRight - The second point making up the line.
     * @return - The area in the circle under the line.
     */
    public static float determineArea(float radius, Vector2f centerOfCircle, Vector2f linePointOnLeft, Vector2f linePointOnRight) {


        float xt1 = linePointOnLeft.x - centerOfCircle.x;
        float yt1 = linePointOnLeft.y - centerOfCircle.y;

        float xt2 = linePointOnRight.x - centerOfCircle.x;
        float yt2 = linePointOnRight.y - centerOfCircle.y;

        float phi = (float) Math.atan2((yt2 - yt1), (xt2 - xt1));
        float lineRotation = -phi;

        //Rotate the line to make the calculations easier.
        Matrix2f rotationMatrix = new Matrix2f();
        rotationMatrix.m00 = (float) Math.cos(lineRotation);
        rotationMatrix.m01 = (float) Math.sin(lineRotation);
        rotationMatrix.m10 = (float) Math.sin(-lineRotation);
        rotationMatrix.m11 = (float) Math.cos(lineRotation);

        Vector2f leftTransformedPoint = new Vector2f(xt1, yt1);
        Vector2f leftRotatedPoint = new Vector2f(leftTransformedPoint);
        rotationMatrix.transform(leftRotatedPoint);

        Vector2f rightTransformedPoint = new Vector2f(xt2, yt2);
        Vector2f rightRotatedPoint = new Vector2f(rightTransformedPoint);
        rotationMatrix.transform(rightRotatedPoint);

        float a = leftRotatedPoint.y;

        if (a < -radius) {
            //The line is below the circle.
            return 0f;
        } else if (a > radius) {
            //The line is above the circle.
            return (float) (Math.PI * radius * radius);
        } else if (a > 0) {
            float totalArea = (float) (Math.PI * radius * radius);
            totalArea -= determineAreaBetweenArcAndLine(radius, a);
            return totalArea;
        }
        //(a <= 0)

        float totalArea = determineAreaBetweenArcAndLine(radius, -a);
        return totalArea;
    }


    /**
     *
     * @param radius - The radius of the circle.
     * @param a - The height above the origin the line is... function is: (y = a)
     * @require a >= 0
     * @return The area between the line and the arc.
     */
    private static float determineAreaBetweenArcAndLine(float radius, float a) {

        float x1 = (float) Math.sqrt((radius * radius) - (a * a));
        float x0 = -x1;

//        float theta = (float) (2.0 * Math.atan2(x1, a));
//        float areaOfCircularWedge = (theta / 2.0f) * radius * radius;

        //Simplification of the above two lines.
        float areaOfCircularWedge = (float) (Math.atan2(x1, a) * radius * radius);
        float areaOfLowerTriangle = ((x1 - x0) * a) / 2.0f;

        return (areaOfCircularWedge - areaOfLowerTriangle);
    }

}
