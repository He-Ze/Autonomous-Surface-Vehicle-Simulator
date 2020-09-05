package gebd.games.boat.entity.pinger;

import composites.entities.Entity;
import renderables.r3D.rotation.Quat4fHelper;
import util.math.RadianAngle;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/**
 * Created by p3te on 21/11/16.
 */
public class PingingBuoy extends Entity {


    public PingingBuoy(String modelPath, String texturePath, Vector3f position, Vector3f size, Vector3f rotation) {
        super(modelPath, texturePath, position, size, rotation);
    }

    /**
     * Will return the real angle as the x component of the vector,
     * and will return the fake angle (reflected angle) as the y component
     * of the vector.
     * @param boatEntity - The boat entity.
     * @return - See above.
     */
    public static Vector2d getPingerAngles(Entity boatEntity, Entity pinger) {

        Quat4f boatRotationQuat4f = boatEntity.getQuatRotation();
        Vector3f boatPosition = boatEntity.getPosition();
        Vector3f boatRotationEuler = new Vector3f();
        Quat4fHelper.toXYZRotation(boatRotationQuat4f, boatRotationEuler);

        Vector3f pingerPosition = pinger.getPosition();

        //Ignore the y component.
        Vector2f toPingerVector = new Vector2f();
        toPingerVector.x = pingerPosition.x - boatPosition.x;
        toPingerVector.y = pingerPosition.z - boatPosition.z;


        //Get the absolute angle to the pinger.
        double toPingerAbsoluteAngle = Math.atan2(toPingerVector.y, toPingerVector.x);

        double boatFacingAngle = boatRotationEuler.x;

        double angleDiffBuoyToFacing = RadianAngle.normalizeAngle(toPingerAbsoluteAngle - boatFacingAngle);

        double reflectionAngle;
        if (angleDiffBuoyToFacing > (Math.PI)) {
            reflectionAngle = boatFacingAngle - (Math.PI / 2.0);
        } else {
            reflectionAngle = boatFacingAngle + (Math.PI / 2.0);
        }

        double fakeAbsoluteAngle = reflectionAngle + (reflectionAngle - toPingerAbsoluteAngle);

        //TODO - Should this be converted into relative angles?

        return new Vector2d(toPingerAbsoluteAngle, fakeAbsoluteAngle);

    }

}