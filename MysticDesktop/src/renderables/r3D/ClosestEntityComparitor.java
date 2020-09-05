package renderables.r3D;

import composites.entities.Entity;
import gebd.camera.Camera;

import javax.vecmath.Vector3f;
import java.util.Comparator;

/**
 * Created by CaptainPete on 2016-11-09.
 */
public class ClosestEntityComparitor implements Comparator<Entity> {

    private Camera camera;

    public ClosestEntityComparitor(Camera camera) {
        this.camera = camera;
    }


    /**
     * Compares two {@code int} values numerically.
     * The value returned is identical to what would be returned by:
     * <pre>
     *    Integer.valueOf(x).compareTo(Integer.valueOf(y))
     * </pre>
     *
     * @param  o1 the first {@code int} to compare
     * @param  o2 the second {@code int} to compare
     * @return the value {@code 0} if {@code x == y};
     *         a value less than {@code 0} if {@code x < y}; and
     *         a value greater than {@code 0} if {@code x > y}
     * @since 1.7
     */
    @Override
    public int compare(Entity o1, Entity o2) {

        Vector3f cameraPos = camera.getPosition();

        //Calc the dist squared to the first object
        Vector3f o1Pos = o1.getPosition();
        float o1xDiff = cameraPos.x - o1Pos.x;
        float o1yDiff = cameraPos.y - o1Pos.y;
        float o1zDiff = cameraPos.z - o1Pos.z;
        float distToCameraE1Squared = (o1xDiff * o1xDiff) + (o1yDiff * o1yDiff) + (o1zDiff * o1zDiff);

        //Calc the dist squared to the second object
        Vector3f o2Pos = o2.getPosition();
        float o2xDiff = cameraPos.x - o2Pos.x;
        float o2yDiff = cameraPos.y - o2Pos.y;
        float o2zDiff = cameraPos.z - o2Pos.z;
        float distToCameraE2Squared = (o2xDiff * o2xDiff) + (o2yDiff * o2yDiff) + (o2zDiff * o2zDiff);

        //We want the objects furthest away to be rendered first, so order it backwards.
        return Float.compare(distToCameraE2Squared, distToCameraE1Squared);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ClosestEntityComparitor)) {
            return false;
        }
        ClosestEntityComparitor other = (ClosestEntityComparitor) obj;
        return camera.equals(other.camera);
    }

}
