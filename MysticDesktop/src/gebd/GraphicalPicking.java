package gebd;

import composites.entities.Entity;
import gebd.shaders.Shader;
import gebd.shaders.Shader3D;
import gebd.shaders.shaders.Picking3DShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import javax.vecmath.Vector4f;
import renderables.framebuffer.FrameBufferObject;
import renderables.texture.TextureInfo;

import java.nio.ByteBuffer;

/**
 * Created by alec on 25/11/15.
 */
public class GraphicalPicking {
    private static int _width;
    private static int _height;

    private static Picking3DShader pickingShader;
    private static FrameBufferObject framebuffer;

    public static void setup(int width, int height) {
        _width = width;
        _height = height;
        framebuffer = new FrameBufferObject(width, height);
    }

    public static void setup(int width, float aspectRatio) {
        _width = width;
        _height = (int) (width / aspectRatio);
        framebuffer = new FrameBufferObject(width, _height);
    }

    public static void setShader(Picking3DShader shader) {
        pickingShader = shader;
    }

    public static int getSelectedEntity(int x, int y, int numberOfEntities) {
        int[] mappedCoords = mapCoords(x, y);

        Render.setShader(pickingShader);
        pickingShader.prepare();
        GL11.glViewport(0, 0, _width, _height);

        framebuffer.bindFrameBuffer();

        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);

        Render.getInstance().renderPickingEntities(pickingShader);

        Vector4f clearColour = Render.getInstance().getClearColour();

        GL11.glClearColor(clearColour.x, clearColour.y, clearColour.z, clearColour.w);

        ByteBuffer colourBuffer = ByteBuffer.allocateDirect(4);
        GL11.glReadPixels(mappedCoords[0], mappedCoords[1], 1, 1, GL11.GL_RGBA, GL11.GL_BYTE, colourBuffer);
        int index = getIndex(colourBuffer);

        GL11.glViewport(0, 0, Render.getWidth(), Render.getHeight());

        framebuffer.unbindCurrentFrameBuffer();

        return index;
    }

    private static Vector4f[] getRenderColours(int number) {
        Vector4f[] colours = new Vector4f[number];
        for (int a = 0; a < number; a++) {
            colours[a] = getUniqueColour(a);
        }
        return colours;
    }

    private static Vector4f getUniqueColour(int index) {
        float value1 = index % 127;
        float temp = (index - value1) / 127;
        float value2 = temp % 127;
        float value3 = (temp - value2) / 127;
        return new Vector4f(value1/127f, value2/127f, value3/127f, 1);
    }

    public static void prepareEntityColour(Shader3D shader, int entityIndex) {
        float value1 = entityIndex % 127;
        float temp = (entityIndex - value1) / 127;
        float value2 = temp % 127;
        float value3 = (temp - value2) / 127;
        shader.setColour(value1/127f, value2/127f, value3/127f, 1);
    }

    private static int getIndex(ByteBuffer colour) {
        if (colour.get(3) != 0) {
            return colour.get(0) + colour.get(1) * 127 + colour.get(2) * 16129;
        }
        return -1;
    }

    private static int[] mapCoords(int x, int y) {
        int[] mappedCoords = new int[2];
        mappedCoords[0] = (int) (((float) x / (float) Render.getInstance().getWidth()) * _width);
        mappedCoords[1] = (int) (((float) y / (float) Render.getInstance().getHeight()) * _height);
        return mappedCoords;
    }

    public static FrameBufferObject getFbo() {
        return framebuffer;
    }

}
