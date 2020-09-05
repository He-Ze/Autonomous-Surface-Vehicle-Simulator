package renderables.r3D.water;

import blindmystics.input.CurrentInput;
import blindmystics.util.FileReader;
import blindmystics.util.GLWrapper;
import gebd.Render;
import gebd.camera.Camera;
import gebd.camera.implementation.WaterFlippedCamera;
import gebd.light.PointLight;
import gebd.shaders.shaders.water.WaterShader;
import loader.LoadedObjectAbstract;
import loader.LoadedObjectHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import renderables.framebuffer.FrameBufferObject;
import renderables.r3D.model.ModelsetModel;
import renderables.r3D.water.wave.WaveSettings;
import renderables.texture.TextureHandler;
import renderables.texture.TextureInfo;
import util.settings.SettingsHolder;

/**
 * Created by CaptainPete on 17/04/2016.
 */
public class RenderedWater extends LoadedObjectAbstract {

    private float waterHeight = 0f;

    private Vector2f waterQuadSize = new Vector2f(120, 120);
//    private int waterResolutionWidth = 320;
//    private int waterResolutionHeight = 180;

    private int waterResolutionWidth = 640;
    private int waterResolutionHeight = 320;

//    private int waterResolutionWidth = 1280;
//    private int waterResolutionHeight = 720;

    private WaterShader waterShader;
    private ModelsetModel waveyModel;
    private ModelsetModel flatModel;
    private ModelsetModel simpleFlatModel;
    private Render render;

    protected WaterFlippedCamera waterFlippedCamera;

    protected FrameBufferObject reflectionFBO;
    protected FrameBufferObject refractionFBO;

    protected TextureInfo dudvMapTexture;
    protected TextureInfo normalMapTexture;

    private LoadedObjectHandler.LoadStatus currentStatus;

    private float waterWaveTime = 0;
    private float waterWaveOffset = 0;
    private float waterRippleOffset = 0;
    private float rippleOffsetSpeed = 0.042f / 1000f;

    private float murkiness = (float) WaterSettings.MURKINESS.getDefaultValue();
    private float waterReflectivity = (float) WaterSettings.WATER_REFLECTIVITY.getDefaultValue();
    private Vector2f tiling = new Vector2f(WaterSettings.DEFAULT_TILING);
    private float waveStrength = (float) WaterSettings.WAVE_STRENGTH.getDefaultValue();
    private float waterRotation = (float) WaterSettings.WATER_ROTATION.getDefaultValue();
    private Vector3f waterColour = new Vector3f(WaterSettings.DEFAULT_WATER_COLOUR);
    private WaveSettings waveSettings = null;
    private float waterWaveHeight = 0f;
    private float waterWaveDirection = 0f;
    private float waterWaveFrequency = 1f;
    private float distanceBetweenWaves = 5.0f;
    private boolean useSimpleWater = true;

    private double[] seaStateAValues = new double[5];
    private double[] seaStateWValues = new double[5];
    private double[] seaStateThetaValues = new double[5];
    private double[] seaStateKValues = new double[5];

    public void setWaterColour(Vector3f waterColour) {
        this.waterColour = waterColour;
    }

    public FrameBufferObject getReflectionFBO() {
        return reflectionFBO;
    }

    public FrameBufferObject getRefractionFBO() {
        return refractionFBO;
    }

    public RenderedWater(WaterShader waterShader, ModelsetModel waveyModel, ModelsetModel flatModel, ModelsetModel simpleFlatModel, Render render, int waterResolutionWidth, int waterResolutionHeight) {
        this.waterResolutionWidth = waterResolutionWidth;
        this.waterResolutionHeight = waterResolutionHeight;
        this.waterShader = waterShader;
        this.waveyModel = waveyModel;
        this.flatModel = flatModel;
        this.simpleFlatModel = simpleFlatModel;
        this.render = render;
        this.waterFlippedCamera = new WaterFlippedCamera();
        if((waterResolutionHeight <= 0) || (waterResolutionWidth <= 0)){
            System.out.println("WARNING: invalid resolution for framebuffer.");
        } else {
            reflectionFBO = new FrameBufferObject(waterResolutionWidth, waterResolutionHeight);
            //Create the FBO is a depth texture attachment instead of a depth buffer.
            refractionFBO = new FrameBufferObject(waterResolutionWidth, waterResolutionHeight, true);
        }
    }

    public Vector2f getWaterQuadSize() {
        return waterQuadSize;
    }

    public void setWaterQuadSize(Vector2f newWaterQuadSize) {
        this.waterQuadSize = newWaterQuadSize;
    }

    public void setWaterResolution(int newWaterResolutionWidth, int newWaterResolutionHeight) {
        this.waterResolutionWidth = newWaterResolutionWidth;
        this.waterResolutionHeight = newWaterResolutionHeight;
    }

    public void setWaveSettings(WaveSettings waveSettings) {
        this.waveSettings = waveSettings;
    }

    public void update(CurrentInput input, float delta) {

        GenerateSeaStateValues();

        if (waveSettings == null) {
            waterWaveFrequency = 1f;
            waterWaveDirection = 0f;
            waterWaveHeight = 0f;
            distanceBetweenWaves = 5.0f;
        } else {
            waterWaveFrequency = waveSettings.getWaterWaveFrequency();
            waterWaveDirection = waveSettings.getWaterDirection();
            waterWaveHeight = waveSettings.getWaterWaveHeight();
            distanceBetweenWaves = waveSettings.getDistanceBetweenWaves();
        }

        if (waterWaveFrequency < 0.1f) {
            //Stop NAN problems.
            waterWaveFrequency = 0.1f;
        }
        //Period of a sin wave is 2*PI.
        float deltaModifier = (float) ((2 * Math.PI) / (1000 * waterWaveFrequency));

        waterWaveTime += (delta / 1000.0f);
        waterWaveOffset += (delta * deltaModifier);
        waterRippleOffset += delta * rippleOffsetSpeed;
        waterFlippedCamera.updateCamera(delta, input);
    }

    public void render(Camera camera, PointLight light, FrameBufferObject frameBufferObjectToRenderTo){

        waterFlippedCamera.setWaterHeight(waterHeight);
        waterFlippedCamera.updateWithParentCamera(camera);

        //Refraction:
        //setClippingPlane(0, -1, 0, water.height)
        refractionFBO.bindFrameBuffer();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GLWrapper.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
        Render.instance.setCurrentClipPlane(0, -1, 0, (-waterHeight) + 0.1f);
        //Render.instance.setCurrentClipPlane(0, 1, 0, waterHeight);
        render.renderWorld(render.getDefault3DShader(), camera);
        GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
        refractionFBO.unbindCurrentFrameBuffer();

        //Reflection:
        //setClippingPlane(0, 1, 0, -water.height)
        reflectionFBO.bindFrameBuffer();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GLWrapper.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
        Render.instance.setCurrentClipPlane(0, 1, 0, waterHeight);
        render.renderWorld(render.getDefault3DShader(), waterFlippedCamera);
        GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
        reflectionFBO.unbindCurrentFrameBuffer();

        if (frameBufferObjectToRenderTo != null) {
            frameBufferObjectToRenderTo.bindFrameBuffer();
        }

        //waterShader
        render.setShader(waterShader);
        waterShader.loadPointLight(light);
        waterShader.setWaterColour(waterColour);
        //TextureHandler.prepareTexture(tempWaterQuadTexure);

        //Load the textures.
        //TODO - Setup the TextureHandler to accept the texture unit.
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, reflectionFBO.getTextureId());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, refractionFBO.getTextureId());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvMapTexture.getTextureId());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMapTexture.getTextureId());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, refractionFBO.getDepthTextureId());
        //GL13.glActiveTexture(GL13.GL_TEXTURE0);

        //Alpha Blending.
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        //dudvMapTexture

        waterShader.setWaterRippleOffset(waterRippleOffset);
        waterShader.setWaterWaveOffset(waterWaveOffset);
        waterShader.setWaterWaveHeight(waterWaveHeight);
        waterShader.setWaterWaveDirection(waterWaveDirection);
        waterShader.setDistanceBetweenWaves(distanceBetweenWaves);
        waterShader.setModelSize(waterQuadSize.x, 1, waterQuadSize.y);

        waterShader.setModelPosition(camera.getPosition().x, 0, camera.getPosition().z);

        waterShader.setWaterMurkiness(murkiness);
        waterShader.setWaterReflectivity(waterReflectivity);
        waterShader.setTiling(tiling);
        waterShader.setWaveStrength(waveStrength);
        waterShader.setWaterRoataion(waterRotation);


        //System.out.println("waterWaveTime = " + waterWaveTime);
        waterShader.setWaterWaveTime(waterWaveTime);


        if (useSimpleWater) {
            Vector2f increasedTiling = new Vector2f(tiling);
            increasedTiling.scale(10);
            waterShader.setTiling(increasedTiling);
            waterShader.setModelSize(waterQuadSize.x * 10, 1, waterQuadSize.y * 10);
            simpleFlatModel.drawElements();

        } else {
            waveyModel.drawElements();

            Vector2f increasedTiling = new Vector2f(tiling);
            increasedTiling.scale(10);
            waterShader.setTiling(increasedTiling);
            waterShader.setModelSize(waterQuadSize.x * 10, 1, waterQuadSize.y * 10);
            flatModel.drawElements();
        }

        waterShader.setSeaStateAValues((toFloatArray(seaStateAValues)));
        waterShader.setSeaStateWValues((toFloatArray(seaStateWValues)));
        waterShader.setSeaStateThetaValues((toFloatArray(seaStateThetaValues)));
        waterShader.setSeaStateKValues((toFloatArray(seaStateKValues)));

        //GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        TextureHandler.prepareTexture(0);

        if (frameBufferObjectToRenderTo != null) {
            frameBufferObjectToRenderTo.unbindCurrentFrameBuffer();
        }

//        GL11.glDisable(GL11.GL_BLEND);
    }

    private static float[] toFloatArray(double[] doubleArr){
        float[] result = new float[doubleArr.length];
        for (int i = 0; i < doubleArr.length; i++) {
            result[i] = (float) doubleArr[i];
        }
        return result;
    }

    public void destroy(){
        reflectionFBO.cleanUp();
    }


    public void GenerateSeaStateValues(){
//        seaStateAValues = new double[]{
//                1.5 * (1.0 / 7.0),
//                1.5 * (0.8 / 6.0),
//                1.5 * (0.6 / 5.0),
//                1.5 * (0.4 / 4.0),
//                1.5 * (0.2 / 3.0)
//        };

        seaStateAValues = new double[]{
                0.0,
                0.0,
                0.0,
                0.0,
                0.0
        };

        seaStateWValues = new double[]{
                1.10,
                1.8,
                1.7,
                1.9,
                1.11
        };

        seaStateThetaValues = new double[]{
                (Math.PI * 0.51),
                (Math.PI * 0.22),
                (Math.PI * 0.31),
                (Math.PI * 1.11),
                (Math.PI * 1.25)
        };

        seaStateKValues = new double[]{
                1.0,
                0.8,
                0.6,
                0.4,
                0.2
        };
    }

    public float getWaterHeight(float xPos, float zPos) {

        GenerateSeaStateValues();

        double waterHeight = 0f;

        for(int i = 0; i < 5; i++){
            waterHeight += seaStateAValues[i] * Math.cos((seaStateWValues[i] * waterWaveTime)
                    + (seaStateKValues[i] * Math.cos(seaStateThetaValues[i])  * xPos)
                    + (seaStateKValues[i] * Math.sin(seaStateThetaValues[i])  * zPos));
        }

        //System.out.println("waterHeight = " + waterHeight);

        return (float) waterHeight;

        //float rotatedXPosition = (float) ((xPos * Math.cos(waterWaveDirection)) - (zPos * Math.sin(waterWaveDirection)));
        //TODO - Consider wave spacing.
        //return waterHeight + (float) (waterWaveHeight * Math.sin((rotatedXPosition / distanceBetweenWaves) + waterWaveOffset));
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
    public LoadedObjectHandler.LoadStage[] stagesToPerform() {
        return new LoadedObjectHandler.LoadStage[] {
                //LoadStage.LOAD_DATA_FROM_FILE,
                //LoadStage.HANDLE_RAW_DATA,
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
        dudvMapTexture = TextureInfo.queueLoadOfPNGTexture(FileReader.asSharedFile("shared/water/waterDUDV.png"), handler);
        normalMapTexture = TextureInfo.queueLoadOfPNGTexture(FileReader.asSharedFile("shared/water/normal.png"), handler);
    }

    @Override
    public void completeLoad(LoadedObjectHandler<?> handler) {
        //Nothing to do here.
    }

    public void updateSettings(SettingsHolder<WaterSettings> waterSettings, boolean useSimpleWater) {
        murkiness = (float) waterSettings.getValue(WaterSettings.MURKINESS);
        waterColour.x = (float) waterSettings.getValue(WaterSettings.RED_COLOUR);
        waterColour.y = (float) waterSettings.getValue(WaterSettings.GREEN_COLOUR);
        waterColour.z = (float) waterSettings.getValue(WaterSettings.BLUE_COLOUR);
        waterReflectivity = (float) waterSettings.getValue(WaterSettings.WATER_REFLECTIVITY);
        tiling.x = (float) waterSettings.getValue(WaterSettings.TILING_X);
        tiling.y = (float) waterSettings.getValue(WaterSettings.TILING_Y);
        waveStrength = (float) waterSettings.getValue(WaterSettings.WAVE_STRENGTH);
        waterRotation = (float) waterSettings.getValue(WaterSettings.WATER_ROTATION);

        //Whether or not to use the simple water. (Better Performance).
        this.useSimpleWater = useSimpleWater;
    }
}
