package physics.buoyancy;

import blindmystics.util.GLWrapper;
import com.bulletphysics.linearmath.Transform;
import physics.util.AreaInCircleUnderLine;
import physics.util.OffsetHelper;
import physics.util.PhysTransform;
import physics.util.Plane;
import renderables.r3D.water.RenderedWater;

import javax.vecmath.*;

/**
 * Created by CaptainPete on 9/11/2016.
 */
public class CylinderBuoyancy {

    public static Vector3f getBouyancyForceOnCylinder(Transform cylinderCenter, float cylinderRadius, float cylinderSegmentHeight,
                                                float cylinderYOffsetFromRigidBody, RenderedWater renderedWater,
                                                float densityOfWater, float gravity) {

        Vector3f offsetToCenterOfCircle = new Vector3f(0, cylinderYOffsetFromRigidBody, 0);
        Vector3f centerOfCircle = PhysTransform.toGlPosition(OffsetHelper.getOffset(cylinderCenter, offsetToCenterOfCircle));

        Vector3f offset1 = new Vector3f(cylinderRadius, cylinderYOffsetFromRigidBody, 0);

        float xOffset2 = (float) Math.sin(Math.PI / 3);
        float zOffset2 = (float) Math.cos(Math.PI / 3);

        Vector3f offset2 = new Vector3f(-zOffset2 * cylinderRadius, cylinderYOffsetFromRigidBody, xOffset2 * cylinderRadius);
        Vector3f offset3 = new Vector3f(-zOffset2 * cylinderRadius, cylinderYOffsetFromRigidBody, -xOffset2 * cylinderRadius);

        Vector3f point1 = PhysTransform.toGlPosition(OffsetHelper.getOffset(cylinderCenter, offset1));
        Vector3f point2 = PhysTransform.toGlPosition(OffsetHelper.getOffset(cylinderCenter, offset2));
        Vector3f point3 = PhysTransform.toGlPosition(OffsetHelper.getOffset(cylinderCenter, offset3));

        //Setup the rotation.
        Vector4f planeEquation = Plane.getPlaneAtPosition2(point1, point2, point3);
        Vector3f planeNormal = new Vector3f(planeEquation.x, planeEquation.y, planeEquation.z);
        Vector3f defaultPlaneNormal = new Vector3f(0, 1, 0);
        AxisAngle4f planeRotationAngles = Plane.rotatePlaneSoUpIsNowOtherThing(defaultPlaneNormal, planeNormal);
        Quat4f planeRotation = new Quat4f();
        planeRotation.set(planeRotationAngles);


        float smallerCircleRadius = 0.5f;

        Vector2f waterXZPosition1 = new Vector2f(centerOfCircle.x + smallerCircleRadius, centerOfCircle.z);
        Vector2f waterXZPosition2 = new Vector2f(centerOfCircle.x - (smallerCircleRadius * zOffset2), centerOfCircle.z - (smallerCircleRadius * xOffset2));
        Vector2f waterXZPosition3 = new Vector2f(centerOfCircle.x - (smallerCircleRadius * zOffset2), centerOfCircle.z + (smallerCircleRadius * xOffset2));


        Vector3f waterPlanePoint1 = getWaterPlanePointAtXZ(waterXZPosition1, renderedWater);
        Vector3f waterPlanePoint2 = getWaterPlanePointAtXZ(waterXZPosition2, renderedWater);
        Vector3f waterPlanePoint3 = getWaterPlanePointAtXZ(waterXZPosition3, renderedWater);

        //Setup the rotation.
        Vector4f waterPlaneEquation = Plane.getPlaneAtPosition2(waterPlanePoint1, waterPlanePoint2, waterPlanePoint3);
        Vector3f waterPlaneNormal = new Vector3f(waterPlaneEquation.x, waterPlaneEquation.y, waterPlaneEquation.z);
        AxisAngle4f waterPlaneRotationAngles = Plane.rotatePlaneSoUpIsNowOtherThing(defaultPlaneNormal, waterPlaneNormal);
        Quat4f waterPlaneRotation = new Quat4f();
        waterPlaneRotation.set(waterPlaneRotationAngles);

        float distanceToCentreOfCircle;
        boolean aboveCenterOfCircle = false;

        float dotProductOfTwoPlanes = planeNormal.dot(new Vector3f(0, 1, 0));
        float cosOfAngleBetween = dotProductOfTwoPlanes / (waterPlaneNormal.length() * planeNormal.length());

        if ((cosOfAngleBetween > 0.95f) || (cosOfAngleBetween < -0.95f)) {

            //The planes are almost, if not, parallel, NAN's can occur if trying to
            //find a point on both planes.
            Vector2f waterXZPositionCenter = new Vector2f(centerOfCircle.x, centerOfCircle.z);
            Vector3f waterPlaneAtCenter = getWaterPlanePointAtXZ(waterXZPositionCenter, renderedWater);
            distanceToCentreOfCircle = Math.abs(centerOfCircle.y - waterPlaneAtCenter.y);

        } else {
            //Find a point on the line that intersects the two planes.
            Vector3f pointOnIntersectionLine = Plane.getPointOnIntersectionOfTwoPlanes(waterPlaneEquation, planeEquation);

            //Find the cross product of the two normals to determine the direction of the line.
            Vector3f normalOfCirclePlane = new Vector3f(planeEquation.x, planeEquation.y, planeEquation.z);
            Vector3f normalOfWaterPlane = new Vector3f(waterPlaneEquation.x, waterPlaneEquation.y, waterPlaneEquation.z);
            Vector3f directionOfIntersectionLine = new Vector3f();
            directionOfIntersectionLine.cross(normalOfCirclePlane, normalOfWaterPlane);

            //Determine the closest point to on the intesection line and the center of the circle.
            Vector3f closestPointOnIntersectingLine = Plane.determineClosestPointOnLineToAPoint2(pointOnIntersectionLine, directionOfIntersectionLine, centerOfCircle);

            //Determine the distance from the center of the circle to the intersection of the two lines.
            Vector3f vectorTowardCenterOfCircle = new Vector3f(closestPointOnIntersectingLine);
            vectorTowardCenterOfCircle.sub(centerOfCircle);
            distanceToCentreOfCircle = vectorTowardCenterOfCircle.length();

            if (closestPointOnIntersectingLine.y > centerOfCircle.y) {
                aboveCenterOfCircle = true;
            }
        }

        if (Float.isNaN(distanceToCentreOfCircle) || (distanceToCentreOfCircle > cylinderRadius)) {

            //It should NEVER be NAN, but just in case it is, this is a contingency.

            Vector2f waterXZPositionCenter = new Vector2f(centerOfCircle.x, centerOfCircle.z);
            Vector3f waterPlaneAtCenter = getWaterPlanePointAtXZ(waterXZPositionCenter, renderedWater);

            //Stops the condition when the distance is so far, strange things happen.
            aboveCenterOfCircle = (waterPlaneAtCenter.y > centerOfCircle.y);
        }


        float areaInCircleUnderWater = AreaInCircleUnderLine.determineArea(cylinderRadius, distanceToCentreOfCircle, aboveCenterOfCircle);

        float volumeOfWaterDisplaced = areaInCircleUnderWater * cylinderSegmentHeight;
        Vector3f buoyancyForce = new Vector3f(waterPlaneNormal);
        buoyancyForce.normalize();
        buoyancyForce.scale(volumeOfWaterDisplaced * densityOfWater * -gravity);
//        Vector3f buoyancyForce = new Vector3f(0, volumeOfWaterDisplaced * DENSITY_OF_WATER * GRAVITY, 0);
        return buoyancyForce;
    }

    private static Vector3f getWaterPlanePointAtXZ(Vector2f xz, RenderedWater water) {
        return new Vector3f(xz.x, water.getWaterHeight(xz.x, xz.y), xz.y);
    }
}
