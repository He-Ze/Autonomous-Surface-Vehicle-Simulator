package gebd;

import composites.entities.Entity;
import gebd.camera.Camera;
import gebd.shaders.shaders.Textured3DShader;
import org.lwjgl.opengl.GL11;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;
import renderables.framebuffer.FrameBufferObject;

import java.util.ArrayList;

/**
 * Created by alec on 12/12/15.
 */
public class SubRender {
    protected FrameBufferObject framebuffer;
    protected Textured3DShader shader;
    protected Vector4f background;
    protected Camera camera;
    protected int width, height;

    protected ArrayList<Entity> entities = new ArrayList<>();

    public SubRender(Textured3DShader shader, Vector4f background, int width, int height, Camera camera, Matrix4f projectionMatrix) {
        this.shader = shader;
        this.background = background;
        this.framebuffer = new FrameBufferObject(width, height);
        this.camera = camera;
        this.height = height;
        this.width = width;

        //camera.overrideProjectionMatrix(projectionMatrix); //TODO - This has changed.
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void render() {
        setBackgroundColour(background);
        GL11.glViewport(0, 0, width, height);
        framebuffer.bindFrameBuffer();
        
        Render.setShader(shader);
        camera.calculateRotationMatrix();
        shader.prepare(camera);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        for (Entity entity : entities) {
            entity.render(shader);
        }

        framebuffer.unbindCurrentFrameBuffer();
    }

    protected void setBackgroundColour(Vector4f colour) {
        GL11.glClearColor(colour.x, colour.y, colour.z, colour.w);
    }

    public FrameBufferObject getFrameBuffer() {
        return framebuffer;
    }

    public void destroy() {
        framebuffer.cleanUp();
    }
    
    public Textured3DShader getShader(){
    	return shader;
    }

	public ArrayList<Entity> getEntities() {
		return entities;
	}
}
