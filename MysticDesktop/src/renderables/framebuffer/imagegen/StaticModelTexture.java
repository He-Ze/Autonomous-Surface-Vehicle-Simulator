package renderables.framebuffer.imagegen;

import blindmystics.util.GLWrapper;
import composites.entities.Entity;
import blindmystics.util.FileReader;
import gebd.ModelsetPlus;
import gebd.Render;
import gebd.camera.Camera;
import gebd.camera.implementation.SuperFixedCamera;
import gebd.light.PointLight;
import gebd.shaders.Shader3D;
import gebd.shaders.shaders.Textured3DShader;
import objUtils.EntRead;
import org.lwjgl.opengl.GL11;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import renderables.framebuffer.FrameBufferObject;
import renderables.r3D.model.ModelsetModel;
import renderables.texture.TextureInfo;

import java.io.InputStream;

/**
 * Created by CaptainPete on 13/06/2016.
 */
public class StaticModelTexture extends FrameBufferObject {

    private ModelsetModel model;
    private TextureInfo modelTexture;
    private Entity modelEntity;
    private Shader3D shader3D;
    private Camera camera;
    private ModelsetPlus modelset;
    private PointLight light = new PointLight(new Vector3f(100, 40, -100), new Vector3f(1, 1, 1), 8f);

    public StaticModelTexture(int width, int height, ModelsetModel model, TextureInfo modelTexture, Shader3D shader3D, ModelsetPlus modelset) {
        super(width * 2, height * 2);
        this.model = model;
        this.modelTexture = modelTexture;
        this.shader3D = shader3D;
        this.modelset = modelset;

        setupCamera();

        setupTexture();
    }

    private void setupCamera(){

        //TODO - This is quite inefficient, it would be better to do this step in the obj parsing and store it in the ENT files.
        String modelEntPath = model.getModelPath();
        InputStream detailFile = FileReader.asInputStream(modelEntPath);
        EntRead detail = new EntRead(detailFile, modelEntPath);

        Vector3f modelMinXYZ = model.getMinimumCoordinates();
        Vector3f modelMaxXYZ = model.getMaximumCoordinates();
        Vector3f modelSize = model.getSize();

        float sizeScaleFactor = 1 / model.getLargestSize();

        Vector3f entityPosition = new Vector3f();
        entityPosition.x = -(modelMinXYZ.x + (modelSize.x / 2f)) * sizeScaleFactor;
        entityPosition.y = -(modelMinXYZ.y + (modelSize.y / 2f)) * sizeScaleFactor;
        entityPosition.z = -(modelMinXYZ.z + (modelSize.z / 2f)) * sizeScaleFactor;

        Vector3f entityRotation = new Vector3f();
        Vector3f entitySize = new Vector3f(sizeScaleFactor, sizeScaleFactor, sizeScaleFactor);

        modelEntity = new Entity("Statically Rendered Model", model, modelTexture, entityPosition, entitySize, entityRotation);


        //Look at (0,0,0)
        float theta = (float) Math.toRadians(90);

//        float theta = (float) (Math.random() * Math.PI * 2);
//        float phi = (float) (Math.random() * Math.PI * 2);

        float phi = (float) Math.toRadians(90);
        Vector2f cameraRotation = new Vector2f(theta, phi);
        Vector3f cameraPosition = new Vector3f(0, 0, 2);
        this.camera = new SuperFixedCamera(cameraPosition, cameraRotation);

    }

    private void setupTexture() {
        bindFrameBuffer();
        float prevAmbientLight = Render.ambientLightIntensity;
        Render.ambientLightIntensity = 1f;

        camera.update(0, null);
        Render.instance.setCurrentCamera(camera);
        Render.setShader(shader3D);

        if(shader3D instanceof Textured3DShader){
            ((Textured3DShader) shader3D).loadPointLight(light);
        }



        GLWrapper.glClearColor(0.2f, 0.2f, 0.2f, 1);
        GLWrapper.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GLWrapper.glEnable(GL11.GL_CULL_FACE);
        GLWrapper.glCullFace(GL11.GL_BACK);

        modelset.bind();
        modelEntity.render(shader3D);

        Render.instance.resetCamera();
        Render.ambientLightIntensity = prevAmbientLight;
        unbindCurrentFrameBuffer();
    }
}
