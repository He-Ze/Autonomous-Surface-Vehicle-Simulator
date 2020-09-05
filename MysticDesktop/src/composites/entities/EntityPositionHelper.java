package composites.entities;

import javax.vecmath.*;

import com.bulletphysics.linearmath.Transform;
import renderables.r3D.object.Has3DPositionAndRotation;
import renderables.r3D.rotation.Quat4fHelper;

/**
 * Created by CaptainPete on 13/06/2016.
 */
public class EntityPositionHelper {


    /**
     * Totally legit translation stuffs.
     * @param parent - The parent to transform off.
     * @param child - The child to transform.
     * @param offsetPosition - The offset position to the parent.
     * @param offsetRotation - The offset rotation from the parent.
     */
    public static void setRelativeTranslation(Has3DPositionAndRotation parent, Has3DPositionAndRotation child, Vector3f offsetPosition, Quat4f offsetRotation) {

        //Vector3f resultantPosition = new Vector3f(offsetPosition);
        //Quat4fHelper.rotateAboutQuaternion(parent.getQuatRotation(), resultantPosition);

        Vector3f resultantPosition = new Vector3f(offsetPosition);
        rotateAboutQuaternion(parent.getQuatRotation(), resultantPosition);

        resultantPosition.add(parent.getPosition());
        child.setPosition(resultantPosition);

        child.getQuatRotation().set(parent.getQuatRotation());
        child.getQuatRotation().mul(offsetRotation);

    }

    public static void rotateAboutQuaternion(Quat4f quat4f, Vector3f position) {
        Matrix3f parentRotationMatrix = new Matrix3f();
        parentRotationMatrix.set(quat4f);
        parentRotationMatrix.transform(position);
    }



    /**
     * TODO - As this is performed on the CPU and not the GPU, it would probably be more efficient to use Quaternions.
     * @param child - The child 3D object that has the position transformed based on it's parent.
     * @param parent - The parent 3D object.
     * @param childPositionOffset - The offset position at which to transform the child.
     * @param childRotationOffset - The offset rotation at which to transform the child.
     */
    public static void setRelativePosition(Has3DPositionAndRotation child, Has3DPositionAndRotation parent, Vector3f childPositionOffset, Vector3f childRotationOffset) {
        RotationPosition rotationPositionOfChild = getRelativePosition(parent, childPositionOffset, childRotationOffset);
        child.setPosition(rotationPositionOfChild.position);
        child.setRotation(rotationPositionOfChild.rotation);
    }

    public static RotationPosition getRelativePosition(Has3DPositionAndRotation parent, Vector3f childPositionOffset, Vector3f childRotationOffset) {
        float parentRotationX = -parent.getRotation().x;
        float parentRotationY = -parent.getRotation().y;
        float parentRotationZ = -parent.getRotation().z;

        /*
             mat4 modelRotXAxis = mat4(
                1, 0, 0, 0,
                0, cos(modelRot.x), -sin(modelRot.x), 0,
                0, sin(modelRot.x), cos(modelRot.x), 0,
                0, 0, 0, 1
            );
         */
        Matrix4f rotationXMatrix = new Matrix4f();
        //m[column][row]
        rotationXMatrix.m00 = 1;
        rotationXMatrix.m11 = (float) Math.cos(parentRotationX);
        rotationXMatrix.m21 = (float) -Math.sin(parentRotationX);
        rotationXMatrix.m12 = (float) Math.sin(parentRotationX);
        rotationXMatrix.m22 = (float) Math.cos(parentRotationX);
        rotationXMatrix.m33 = 1;

        /*
        	mat4 modelRotYAxis = mat4(
                cos(modelRot.y), 0, sin(modelRot.y), 0,
                0, 1, 0, 0,
                -sin(modelRot.y), 0, cos(modelRot.y), 0,
                0, 0, 0, 1
            );
         */
        Matrix4f rotationYMatrix = new Matrix4f();
        //m[column][row]
        rotationYMatrix.m00 = (float) Math.cos(parentRotationY);
        rotationYMatrix.m20 = (float) Math.sin(parentRotationY);
        rotationYMatrix.m11 = 1;
        rotationYMatrix.m02 = (float) -Math.sin(parentRotationY);
        rotationYMatrix.m22 = (float) Math.cos(parentRotationY);
        rotationYMatrix.m33 = 1;

        /*
            mat4 modelRotZAxis = mat4(
                cos(modelRot.z), -sin(modelRot.z), 0, 0,
                sin(modelRot.z), cos(modelRot.z), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
            );
         */
        Matrix4f rotationZMatrix = new Matrix4f();
        //m[column][row]
        rotationZMatrix.m00 = (float) Math.cos(parentRotationZ);
        rotationZMatrix.m10 = (float) -Math.sin(parentRotationZ);
        rotationZMatrix.m22 = 1;
        rotationZMatrix.m01 = (float) Math.sin(parentRotationZ);
        rotationZMatrix.m11 = (float) Math.cos(parentRotationZ);
        rotationZMatrix.m33 = 1;

        //Perform the rotations.
        Vector4f positionOffset = new Vector4f(childPositionOffset.x, childPositionOffset.y, childPositionOffset.z, 0);
        rotationZMatrix.transform(positionOffset);
        rotationXMatrix.transform(positionOffset);
        rotationYMatrix.transform(positionOffset);

        float childFinalPositionX = parent.getPosition().x + positionOffset.x;
        float childFinalPositionY = parent.getPosition().y + positionOffset.y;
        float childFinalPositionZ = parent.getPosition().z + positionOffset.z;

        //Offset the rotation by the desired amount.
        Vector3f finalPosition = new Vector3f(childFinalPositionX, childFinalPositionY, childFinalPositionZ);
        Vector3f finalRotation = new Vector3f(-parentRotationX + childRotationOffset.x,
                parentRotationY + childRotationOffset.y,
                -parentRotationZ + childRotationOffset.z);
        return new RotationPosition(finalPosition, finalRotation);
    }

}
