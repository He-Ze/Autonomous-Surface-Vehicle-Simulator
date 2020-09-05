package renderables.r3D.model;

import gebd.Render;
import org.lwjgl.opengl.GL11;
import javax.vecmath.Vector3f;

/**
 * Created by alec on 5/01/16.
 */
public class AnimatedModelsetModel extends ModelsetModel {
    private int[] animationPointers;
    private int[] lengths;

    private int currentFrame = 0;
    private double lastFrame = -1;

    private Vector3f[] minimums, maximums;

    public AnimatedModelsetModel(String name, String modelENTPath, int pointer, int[] animationPointers, int[] lengths, float[][] minimums, float[][] maximums) {
        super(name, modelENTPath, pointer, lengths[0], minimums[0], maximums[0]);

        for (int a = 0; a < minimums.length; a++) {
            this.minimums[a] = new Vector3f(minimums[a][0], minimums[a][1], minimums[a][2]);
        }

        for (int a = 0; a < maximums.length; a++) {
            this.maximums[a] = new Vector3f(maximums[a][0], maximums[a][1], maximums[a][2]);
        }

        this.animationPointers = animationPointers;
        for (int a = 0; a < animationPointers.length; a++) {
            animationPointers[a] = (animationPointers[a] + pointer) * 4; //Converting to BYTEs
        }

        this.lengths = lengths;
    }

    public AnimatedModelsetModel(String name, String modelEntPath, int pointer, int[] animationPointers, int[] lengths, float[][] minimums, float[][] maximums, PhysicsModelsetModel physics, ModelsetModel graphicalPicking) {
        this(name, modelEntPath, pointer, animationPointers, lengths, minimums, maximums);
        this.physics = physics;
        this.graphicalPicking = graphicalPicking;
    }

    @Override
    public void drawElements() {
        double now = Render.getInstance().getTime();
        if (lastFrame == -1) {
            lastFrame = now;
        } else {
            double difference = now - lastFrame;
            double numberOfSkippedFrames = difference / 16.77d;
            if (numberOfSkippedFrames > 1d) {
                currentFrame = (currentFrame + (int) numberOfSkippedFrames) % (animationPointers.length - 1);
                lastFrame = now; //Extra calculation required to ensure that amount of extra time is preserved
            }
        }
        GL11.glDrawElements(GL11.GL_TRIANGLES, lengths[currentFrame], GL11.GL_UNSIGNED_INT, animationPointers[currentFrame]);
    }

    @Override
    public Vector3f getMinimumCoordinates() {
        return minimums[currentFrame];
    }

    @Override
    public Vector3f getMaximumCoordinates() {
        return maximums[currentFrame];
    }
}
