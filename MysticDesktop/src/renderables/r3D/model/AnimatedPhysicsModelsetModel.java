package renderables.r3D.model;

import gebd.Render;
import org.lwjgl.opengl.GL11;
import renderables.Vertex;

/**
 * Created by alec on 5/01/16.
 */
public class AnimatedPhysicsModelsetModel extends PhysicsModelsetModel { //TODO this
    private int[] animationPointers;
    private int[] lengths;

    private int currentFrame = 0;
    private double lastFrame = -1;

    public AnimatedPhysicsModelsetModel(String name, String modelENTPath, int pointer, int[] animationPointers, int[] lengths, Vertex[] vertices) {
        super(name, modelENTPath, pointer, lengths[0], vertices);

        this.animationPointers = animationPointers;
        for (int a = 0; a < animationPointers.length; a++) {
            animationPointers[a] = (animationPointers[a] + pointer) * 4; //Converting to BYTEs
        }

        this.lengths = lengths;
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
}
