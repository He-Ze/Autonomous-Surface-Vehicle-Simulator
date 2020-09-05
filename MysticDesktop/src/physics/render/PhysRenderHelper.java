package physics.render;

import com.bulletphysics.linearmath.Transform;
import composites.entities.Entity;
import physics.util.PhysTransform;

import javax.vecmath.Quat4f;
import javax.vecmath.Tuple4f;

/**
 * Created by CaptainPete on 9/4/2016.
 */
public class PhysRenderHelper {

    public static void prepareEntity(Entity entity, Transform transform, javax.vecmath.Vector3f size) {
        setEntityTransform(entity, transform);
        setEntitySize(entity, PhysTransform.toGlPosition(size));
    }

    public static void setEntityTransform(Entity entity, Transform transform) {
        javax.vecmath.Vector3f glBoxPosition = PhysTransform.toGlPosition(transform.origin);
        setEntityPosition(entity, glBoxPosition);
        Quat4f glBoxRotation = PhysTransform.toGlRotation(transform.basis);
        setEntityRotation(entity, glBoxRotation);
    }

    public static void setEntityPosition(Entity entity, javax.vecmath.Vector3f position) {
        entity.setPosition(position.x, position.y, position.z);
    }

    public static void setEntitySize(Entity entity, javax.vecmath.Vector3f size) {
        entity.setSize(size.x, size.y, size.z);
    }

    public static void setEntityRotation(Entity entity, Tuple4f tuple4f) {
        entity.getQuatRotation().set(tuple4f);
        entity.getQuatRotation().inverse();
    }
}
