package blindmystics.util.vector;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 9/8/2016.
 */
public class Matrix4fHelper {

    public static void translate(Matrix4f src, Vector3f vec) {
        src.m30 += src.m00 * vec.x + src.m10 * vec.y + src.m20 * vec.z;
        src.m31 += src.m01 * vec.x + src.m11 * vec.y + src.m21 * vec.z;
        src.m32 += src.m02 * vec.x + src.m12 * vec.y + src.m22 * vec.z;
        src.m33 += src.m03 * vec.x + src.m13 * vec.y + src.m23 * vec.z;
    }

}
