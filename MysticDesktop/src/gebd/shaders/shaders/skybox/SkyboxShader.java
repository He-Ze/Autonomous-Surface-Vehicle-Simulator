package gebd.shaders.shaders.skybox;

import gebd.Render;
import gebd.camera.Camera;
import gebd.shaders.Shader;
import org.lwjgl.opengl.GL20;
import javax.vecmath.Matrix4f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 16/04/2016.
 */
public class SkyboxShader extends Shader {

    protected int projectionRotationLocation;
    protected int textureBlendColourLocation;
    protected int textureBlendAmountLocation;

    public SkyboxShader(int vertexShaderId, int fragmentShaderId){
        super(vertexShaderId, fragmentShaderId);
    }

    @Override
    protected void bindAttributeLocations() {
        // Position information will be attribute 0
        GL20.glBindAttribLocation(programId, 0, "position");

        //Texture Coords
        GL20.glBindAttribLocation(programId, 1, "textureCoords");
    }

    @Override
    protected void setupUniformVariables() {
        // 3D //
        projectionRotationLocation = getUniformLocation("projectionRotationMatrix");

        textureBlendColourLocation = getUniformLocation("textureBlendColour");
        textureBlendAmountLocation = getUniformLocation("textureBlendAmount");
    }

    @Override
    public void prepare() {
        Camera camera = Render.instance.getCamera();
        prepare(camera);
    }

    public void prepare(Camera camera) {
        Matrix4f projectionRotation = camera.getProjectionRotationMatrix();
        setProjectionRotationMatrix(projectionRotation);
        sendToShaderTextureBlendColour(new Vector3f(0.5f, 0.5f, 1f));
        sendToShaderTextureBlendAmount(0f);
    }

    public void sendToShaderTextureBlendColour(Vector3f colour) {
        sendToShaderTextureBlendColour(colour.x, colour.y, colour.z);
    }

    public void sendToShaderTextureBlendColour(Tuple3f colour) {
        sendToShaderTextureBlendColour(colour.x, colour.y, colour.z);
    }

    public void sendToShaderTextureBlendColour(float red, float green, float blue) {
        loadVec3(textureBlendColourLocation, red, green, blue);
    }

    public void sendToShaderTextureBlendAmount(float blendAmount) {
        loadFloat(textureBlendAmountLocation, blendAmount);
    }

    public void setProjectionRotationMatrix(Matrix4f projectionRotationMatrix){
        loadMatrix4f(projectionRotationLocation, projectionRotationMatrix);
    }
}
