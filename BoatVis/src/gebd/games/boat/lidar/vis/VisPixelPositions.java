package gebd.games.boat.lidar.vis;

import blindmystics.util.GLWrapper;
import gebd.Render;
import gebd.games.boat.lidar.LidarHelper;
import gebd.games.boat.lidar.LidarReading;
import gebd.shaders.Shader2D;
import org.lwjgl.opengl.GL11;
import renderables.framebuffer.FrameBufferObject;
import renderables.r2D.Quad;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.simple.SimpleQuad;
import renderables.texture.TextureHandler;
import renderables.texture.generated.SolidFillTextureHandler;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;
import java.util.ArrayList;

/**
 * Created by CaptainPete on 2016-10-29.
 */
public class VisPixelPositions extends UserInterface {

    private LidarHelper lidarHelper;
    private SimpleQuad simpleQuad;
    private FrameBufferObject frameBufferObject;
    private LidarHelper.DepthBufferDirection depthBufferDirection;

    public VisPixelPositions(Vector2f relativePosition, Vector2f size, float rotation, LidarHelper lidarHelper, LidarHelper.DepthBufferDirection depthBufferDirection) {
        super(relativePosition, size, rotation);
        this.lidarHelper = lidarHelper;
        this.depthBufferDirection = depthBufferDirection;

        generateFrameBuffer();

        simpleQuad = addComponentToTop(new SimpleQuad(
                frameBufferObject.getTexture(),
                new Vector2f(0f, 0f),
                size,
                0f
        ));
        simpleQuad.setTextureIsFlippedVertically(true);

        /*
        int ordinalId = LidarHelper.DepthBufferDirection.FORWARD.ordinal();
        ArrayList<LidarReading> directionalReadings = lidarHelper.getBufferSpecificLidarRawReadings().get(ordinalId);


        for (int i = 0; i < directionalReadings.size(); i++) {
            LidarReading lidarReading = directionalReadings.get(i);

            float xPos = (lidarReading.getScreenXPercentage() * size.x) - (size.x / 2.0f) + 1;
            float yPos = (lidarReading.getScreenYPercentage() * size.y) - (size.y / 2.0f);

            SimpleQuad quad = addComponentToTop(new SimpleQuad(
                    new Vector2f(xPos, yPos),
                    new Vector2f(1f, 1f),
                    0f
            ));
            quad.setBlend(new Vector4f(1f, 0, 0, 1f), 1f);

        }
        */

    }

    private void generateFrameBuffer(){
        frameBufferObject = new FrameBufferObject((int) getSize().x, (int) getSize().y);

        frameBufferObject.bindFrameBuffer();

        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GLWrapper.glClearColor(0f, 1f, 1f, 0.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        //2D setup
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        Quad.bind();
        Shader2D shader2D = Render.instance.getDefault2DShader();
        Render.setShader(shader2D);
        shader2D.setScreenResolution(getSize());
        TextureHandler.prepareTexture(SolidFillTextureHandler.getSolidFillTexture());

        int ordinalId = depthBufferDirection.ordinal();
        ArrayList<LidarReading> directionalReadings = lidarHelper.getBufferSpecificLidarRawReadings().get(ordinalId);

        for (int i = 0; i < directionalReadings.size(); i++) {
            LidarReading lidarReading = directionalReadings.get(i);

            float xPos = (lidarReading.getScreenXPercentage() * size.x) + 1;
            float yPos = (lidarReading.getScreenYPercentage() * size.y);

            shader2D.setQuadLocation(xPos, yPos);
            shader2D.setQuadSize(1f, 1f);
            shader2D.setColour(1f, 0f, 0f, 1f);
            shader2D.setMixAmount(1f);
            Quad.render();

        }

        frameBufferObject.unbindCurrentFrameBuffer();
    }
}
