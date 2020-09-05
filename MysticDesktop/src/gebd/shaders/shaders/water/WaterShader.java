package gebd.shaders.shaders.water;

import blindmystics.util.GLWrapper;
import composites.entities.Entity;
import gebd.Render;
import gebd.camera.Camera;
import gebd.light.PointLight;
import gebd.shaders.Shader;
import org.lwjgl.opengl.GL20;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import renderables.r3D.water.WaterSettings;
import renderables.texture.TextureHandler;
import renderables.texture.TextureInfo;

/**
 * Created by CaptainPete on 17/04/2016.
 */
public class WaterShader extends Shader{

    protected int projectionRotationLocation;
    protected int viewPosLocation;
    protected int modelPosLocation;
    protected int modelScaleLocation;



    protected int lightPositionLocation;
    protected int lightColourLocation;
    protected int lightStrengthLocation;
    protected int ambientLightIntensityLocation;


    protected int waterRippleOffsetLocation;

    protected int waterWaveOffsetLocation;
    protected int waterWaveHeightLocation;
    protected int waterWaveRotationLocation;
    protected int distanceBetweenWavesLocation;

    protected int tilingLocation;
    protected int waterColourLocation;
    protected int waterMurkinessLocation;
    protected int waterReflectivityLocation;
    protected int waveStrengthLocation;

    protected int rotationWaterLocation;

    protected int reflectionTextureLocation;
    protected int refractionTextureLocation;
    protected int dudvMapTextureLocation;
    protected int normalMapTextureLocation;
    protected int depthMapLocation;

    protected int waterWaveTimeLocation;
    protected int seaStateAValuesLocation;
    protected int seaStateWValuesLocation;
    protected int seaStateThetaValuesLocation;
    protected int seaStateKValuesLocation;

    /**
     * Constructor
     */
    public WaterShader(int vertexShaderId, int fragmentShaderId){
        super(vertexShaderId, fragmentShaderId);
    }




    /*
     * Shader3D
     */
    public void setProjectionRotationMatrix(Matrix4f projectionRotationMatrix){
        loadMatrix4f(projectionRotationLocation, projectionRotationMatrix);
    }

    public void setCameraPosition(Vector3f cameraPosition){
        loadVec4(viewPosLocation, cameraPosition.x, cameraPosition.y, cameraPosition.z, 0);
    }

    public void setModelPosition(Vector3f modelPosition){
        loadVec4(modelPosLocation, modelPosition.x, modelPosition.y, modelPosition.z, 1);
    }
    public void setModelPosition(float x, float y, float z){
        loadVec4(modelPosLocation, x, y, z, 1);
    }

    public void setModelSize(Vector3f modelSize){
        loadVec4(modelScaleLocation, modelSize, 1);
    }
    public void setModelSize(float x, float y, float z) {
        loadVec4(modelScaleLocation, x, y, z, 1);
    }





    /*
     * TexturedShader3D
     */
    @Override
    protected void bindAttributeLocations() {
		/*
			in vec4 in_Position;
			in vec2 in_TextureCoord;
			in vec3 normal;

			out vec2 pass_TextureCoord;
			out vec3 surfaceNormal;
			out vec3 toLightVector;
			out float lightDistance;
		 */

        // Position information will be attribute 0
        GL20.glBindAttribLocation(programId, 0, "in_Position");

        GL20.glBindAttribLocation(programId, 1, "in_TextureCoord");

        GL20.glBindAttribLocation(programId, 2, "normal");

    }

    @Override
    protected void setupUniformVariables() {
        //Get uniform locations

        // 3D //
        projectionRotationLocation = getUniformLocation("projectionRotationMatrix");

        viewPosLocation = getUniformLocation("viewPos");

        modelPosLocation = getUniformLocation("modelPos");
        modelScaleLocation = getUniformLocation("modelSize");

        lightPositionLocation = getUniformLocation("lightPosition");
        lightColourLocation = getUniformLocation("lightColour");
        lightStrengthLocation = getUniformLocation("luminosity");

        waterRippleOffsetLocation = getUniformLocation("waterRippleOffset");

        waterWaveOffsetLocation = getUniformLocation("waterWaveOffset");
        waterWaveRotationLocation = getUniformLocation("waterWaveDirection");
        waterWaveHeightLocation = getUniformLocation("waterWaveHeight");
        distanceBetweenWavesLocation = getUniformLocation("distanceBetweenWaves");

        waterMurkinessLocation = getUniformLocation("waterMurkiness");
        waterReflectivityLocation = getUniformLocation("waterReflectivity");

        ambientLightIntensityLocation = getUniformLocation("ambientLightIntensity");

        waterColourLocation = getUniformLocation("waterColour");

        tilingLocation = getUniformLocation("tiling");

        waveStrengthLocation = getUniformLocation("waveStrength");

        rotationWaterLocation = getUniformLocation("rotationWater");

        //Texturing.
        reflectionTextureLocation = getUniformLocation("reflectionTexture");
        refractionTextureLocation = getUniformLocation("refractionTexture");
        dudvMapTextureLocation = getUniformLocation("dudvMapTexture");
        normalMapTextureLocation = getUniformLocation("normalMapTexture");
        depthMapLocation = getUniformLocation("depthMap");

        waterWaveTimeLocation = getUniformLocation("waterWaveTime");
        seaStateAValuesLocation = getUniformLocation("seaStateAValues");
        seaStateWValuesLocation = getUniformLocation("seaStateWValues");
        seaStateThetaValuesLocation = getUniformLocation("seaStateThetaValues");
        seaStateKValuesLocation = getUniformLocation("seaStateKValues");
    }

    @Override
    public void prepare() {
        Camera camera = Render.instance.getCamera();
        prepare(camera);
    }

    public void prepare(Camera camera) {
        Matrix4f projectionRotation = camera.getProjectionRotationMatrix();
        setProjectionRotationMatrix(projectionRotation);

        Vector3f cameraPos = camera.getPosition();
        setCameraPosition(cameraPos);

        setAmbientLightIntensity(Render.ambientLightIntensity);

        setWaterRippleOffset(0);
        setWaterMurkiness((Float) WaterSettings.MURKINESS.getDefaultValue());
        setWaterReflectivity((Float) WaterSettings.WATER_REFLECTIVITY.getDefaultValue());
        setTiling(WaterSettings.DEFAULT_TILING);
        setWaveStrength((Float) WaterSettings.WAVE_STRENGTH.getDefaultValue());
        setWaterRoataion((Float) WaterSettings.WATER_ROTATION.getDefaultValue());

        setWaterColour(WaterSettings.DEFAULT_WATER_COLOUR);

        connectTextureUnits();
    }

    public void prepareEntity(Entity e){
        setTexture(e.getTexture());
        setModelPosition(e.getPosition());
        setModelSize(e.getSize());
    }

    public void connectTextureUnits(){
        loadInt(reflectionTextureLocation, 0);
        loadInt(refractionTextureLocation, 1);
        loadInt(dudvMapTextureLocation, 2);
        loadInt(normalMapTextureLocation, 3);
        loadInt(depthMapLocation, 4);
    }

    public void setWaveStrength(float waveStrength) {
        loadFloat(waveStrengthLocation, waveStrength);
    }

    public void setWaterRoataion(float waterRotation) {
        loadFloat(rotationWaterLocation, waterRotation);
    }

    public void setWaterMurkiness(float murkiness) {
        loadFloat(waterMurkinessLocation, murkiness);
    }

    public void setWaterReflectivity(float waterReflectivity){
        loadFloat(waterReflectivityLocation, waterReflectivity);
    }

    public void setTiling(Vector2f tiling) {
        setTiling(tiling.x, tiling.y);
    }

    public void setTiling(float tilingX, float tilingY) {
        loadVec2(tilingLocation, tilingX, tilingY);
    }

    public void setAmbientLightIntensity(float ambientLightIntensity){
        loadFloat(ambientLightIntensityLocation, ambientLightIntensity);
    }

    public void setWaterRippleOffset(float rippleOffset) {
        loadFloat(waterRippleOffsetLocation, rippleOffset);
    }

    public void setWaterWaveOffset(float waveOffset) {
        loadFloat(waterWaveOffsetLocation, waveOffset);
    }

    public void setWaterWaveHeight(float waveHeight) {
        loadFloat(waterWaveHeightLocation, waveHeight);
    }

    public void setWaterWaveDirection(float waveRotation) {
        loadFloat(waterWaveRotationLocation, waveRotation);
    }

    public void setDistanceBetweenWaves(float distanceBetweenWaves) {
        loadFloat(distanceBetweenWavesLocation, distanceBetweenWaves);
    }




    public void setTextureOffset(Vector2f textureOffset){
        throw new UnsupportedOperationException();
    }

    public void setTexture(TextureInfo texture) {
        TextureHandler.prepareTexture(texture);
    }

    public void setWaterColour(Vector3f waterColour) {
        setWaterColour(waterColour.x, waterColour.y, waterColour.z);
    }

    public void setWaterColour(float red, float green, float blue) {
        loadVec3(waterColourLocation, red, green, blue);
    }

    public void loadPointLight(PointLight light){
        loadVec3(lightPositionLocation, light.getPosition());
        loadVec3(lightColourLocation, light.getColour());
        loadFloat(lightStrengthLocation, light.getLuminosity());
    }

    public void setWaterWaveTime(float waterWaveTime){
        loadFloat(waterWaveTimeLocation, waterWaveTime);
    }

    public void setSeaStateAValues(float[] seaStateAValues){
        GLWrapper.glUniform1(seaStateAValuesLocation, seaStateAValues);
    }

    public void setSeaStateWValues(float[] seaStateWValues){
        GLWrapper.glUniform1(seaStateWValuesLocation, seaStateWValues);
    }

    public void setSeaStateThetaValues(float[] seaStateThetaValues){
        GLWrapper.glUniform1(seaStateThetaValuesLocation, seaStateThetaValues);
    }

    public void setSeaStateKValues(float[] seaStateKValues){
        GLWrapper.glUniform1(seaStateKValuesLocation, seaStateKValues);
    }

}
