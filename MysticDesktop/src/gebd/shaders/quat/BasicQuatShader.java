package gebd.shaders.quat;

import blindmystics.util.GLWrapper;
import composites.entities.Entity;
import gebd.Render;
import gebd.camera.Camera;
import gebd.shaders.Shader3D;
import javax.vecmath.Vector3f;
import renderables.texture.TextureHandler;

import javax.vecmath.Quat4f;
import javax.vecmath.Tuple4f;

/**
 * Created by P3TE on 28/08/16.
 */
public class BasicQuatShader extends Shader3D {
    public BasicQuatShader(int vertexShaderId, int fragmentShaderId) {
        super(vertexShaderId, fragmentShaderId);
    }

    @Override
    public void prepareEntity(Entity entity) {
        TextureHandler.prepareTexture(entity.getTexture());
        setModelPosition(entity.getPosition());
        setModelRotation(entity.getQuatRotation());
        setModelSize(entity.getSize());
    }

    @Override
    protected void setupUniformVariables() {
        modelPosLocation = GLWrapper.glGetUniformLocation(programId, "modelPos");
        modelScaleLocation = GLWrapper.glGetUniformLocation(programId, "modelSize");
        modelRotationLocation = GLWrapper.glGetUniformLocation(programId, "modelRot");
        System.out.println("modelRotationLocation: " + modelRotationLocation);
        projectionRotationLocation = GLWrapper.glGetUniformLocation(programId, "projectionRotationMatrix");
        viewPosLocation = GLWrapper.glGetUniformLocation(programId, "viewPos");
    }

    @Override
    public void prepare() {
        Camera camera = Render.getInstance().getCamera();
        setProjectionRotationMatrix(camera.getProjectionRotationMatrix());
        setCameraPosition(camera.getPosition());
    }

    @Override
    protected void bindAttributeLocations() {
        // Position information will be attribute 0
        GLWrapper.glBindAttribLocation(programId, 0, "in_Position");
        // UV information will be attribute 1
        GLWrapper.glBindAttribLocation(programId, 1, "in_TextureCoord");
        // Normal information will be attribute 2
        GLWrapper.glBindAttribLocation(programId, 2, "normal");
    }

    @Override
    public void setModelRotation(float x, float y, float z) {
        return;
        //throw new UnsupportedOperationException();
    }

    @Override
    public void setModelRotation(Vector3f modelRotation) {
        return;
        //throw new UnsupportedOperationException();
    }

    private void setModelRotation(float qx, float qy, float qz, float qw){
        loadVec4(modelRotationLocation, qx, qy, qz, qw);
    }

    public void setModelRotation(Tuple4f quat4f) {
        setModelRotation(quat4f.x, quat4f.y, quat4f.z, quat4f.w);
    }
}
