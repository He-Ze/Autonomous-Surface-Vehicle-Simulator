package renderables.r3D.skybox;

import blindmystics.util.FileReader;
import gebd.Render;
import gebd.camera.Camera;
import gebd.shaders.shaders.skybox.SkyboxShader;
import loader.LoadedObjectAbstract;
import loader.LoadedObjectHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import renderables.texture.CubemapTexture;
import renderables.texture.TextureHandler;

import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 16/04/2016.
 */
public class Skybox extends LoadedObjectAbstract {

    private static final float SIZE = 500; //Consider - This is the MAX distance for all renderable objects. (A 500X500 Cube)

    private LoadedObjectHandler.LoadStatus currentStatus;

    private CubemapTexture cubemapTexture;
    private SkyboxShader shader;
    private RawModel cube;
    private Vector3f skyboxBlendColour = new Vector3f(1f, 1f, 1f);
    private float skyboxBlendAmount = 0f;

    public Skybox(SkyboxShader shader){
        this(getDefaultSkybox(), shader);
    }

    public Skybox(CubemapTexture cubemapTexture, SkyboxShader shader){
        this.cubemapTexture = cubemapTexture;
        this.shader = shader;
        this.cube = RawModel.loadToVAO(VERTICES, 3);
    }


    public LoadedObjectHandler.LoadStage[] stagesToPerform(){
        return new LoadedObjectHandler.LoadStage[] {
//              LoadedObjectHandler.LoadStage.LOAD_DATA_FROM_FILE,
//				LoadStage.HANDLE_RAW_DATA,
                LoadedObjectHandler.LoadStage.LOAD_DEPENDENCIES,
        };
    }

    @Override
    public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
        //Not called.
    }

    @Override
    public void handleRawData(LoadedObjectHandler<?> handler) {
        //Not called.
    }

    @Override
    public void loadDependencies(LoadedObjectHandler<?> handler) {
        handler.newDependancy(cubemapTexture);
    }

    @Override
    public LoadedObjectHandler.LoadStatus getLoadStatus() {
        return currentStatus;
    }

    @Override
    public void setLoadStatus(LoadedObjectHandler.LoadStatus newLoadStatus) {
        this.currentStatus = newLoadStatus;
    }

    @Override
    public void completeLoad(LoadedObjectHandler<?> handler) {

    }

    public Vector3f getSkyboxBlendColour() {
        return skyboxBlendColour;
    }

    public void setSkyboxBlendColour(Vector3f skyboxBlendColour) {
        this.skyboxBlendColour = skyboxBlendColour;
    }

    public float getSkyboxBlendAmount() {
        return skyboxBlendAmount;
    }

    public void setSkyboxBlendAmount(float skyboxBlendAmount) {
        this.skyboxBlendAmount = skyboxBlendAmount;
    }

    public void renderSkybox(Camera camera){
        Render.setShader(shader);
        shader.prepare(camera);
        shader.sendToShaderTextureBlendColour(skyboxBlendColour);
        shader.sendToShaderTextureBlendAmount(skyboxBlendAmount);
        GL30.glBindVertexArray(cube.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        TextureHandler.prepareTexture(GL13.GL_TEXTURE_CUBE_MAP, cubemapTexture.getTextureId());
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        //DONE.
    }

    /**
     * Generates the textures for the cubemap texture.
     * More skyboxes can be found at:
     * http://www.custommapmakers.org/skyboxes.php
     * =D
     * @return
     */
    public static CubemapTexture getDefaultSkybox(){
        String sharedFolder = FileReader.asSharedFile("shared/skybox/textures/");
        CubemapTexture cubemapTexture = new CubemapTexture(
                sharedFolder + "right.png",
                sharedFolder + "left.png",
                sharedFolder + "top.png",
                sharedFolder + "bottom.png",
                sharedFolder + "back.png",
                sharedFolder + "front.png"
        );
        return cubemapTexture;
    }

    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };






}
