package renderables.r3D.model;

import javax.vecmath.Vector3f;
import renderables.Vertex;

/**
 * Created by alec on 11/01/16.
 */
public class PhysicsModelsetModel extends ModelsetModel {
    private Vertex[] vertices;

    private boolean sizeCalculated = false;
    private Vector3f size = null;
    private Vector3f min = new Vector3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
    private Vector3f max = new Vector3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

    public PhysicsModelsetModel(String name, String modelENTPath, int pointer, int length, Vertex[] vertices) {
        super(name, modelENTPath, pointer, length, new float[3], new float[3]); // TODO replace with mins and maxes
        this.vertices = vertices;
    }

    public Vector3f getSize() {
        if (!sizeCalculated) {
            float[] v;
            for (Vertex vertex : vertices) {
                v = vertex.getXYZ();
                if (v[0] < min.x) {
                    min.x = v[0];
                } else if (v[0] > max.x) {
                    max.x = v[0];
                }

                if (v[1] < min.y) {
                    min.y = v[1];
                } else if (v[1] > max.y) {
                    max.y = v[1];
                }

                if (v[2] < min.z) {
                    min.z = v[2];
                } else if (v[2] > max.z) {
                    max.z = v[2];
                }
            }
            size = new Vector3f(max.x - min.x, max.y - min.y, max.z - min.z);
        }
        return size;
    }

    public Vector3f getMinPosition() {
        if (!sizeCalculated) {
            getSize();
        }
        return min;
    }

    public Vector3f getMaxPosition() {
        if (!sizeCalculated) {
            getSize();
        }
        return max;
    }

    public float getLargestSize() {
        if (!sizeCalculated) {
            getSize();
        }
        if (size.x >= size.y && size.x >= size.z) {
            return size.x;
        }
        if (size.y >= size.x && size.y >= size.z) {
            return size.y;
        }
        if (size.z >= size.x && size.z >= size.y) {
            return size.z;
        }
        return 0;
    }

    public float getMinimumSize() {
        if (!sizeCalculated) {
            getSize();
        }
        if (size.x <= size.y && size.x <= size.z) {
            return size.x;
        }
        if (size.y <= size.x && size.y <= size.z) {
            return size.y;
        }
        if (size.z <= size.x && size.z <= size.y) {
            return size.z;
        }
        return 0;
    }
}
