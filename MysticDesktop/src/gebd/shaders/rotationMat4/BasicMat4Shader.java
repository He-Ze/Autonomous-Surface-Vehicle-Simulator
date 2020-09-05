package gebd.shaders.rotationMat4;

import blindmystics.util.GLWrapper;
import composites.entities.Entity;
import gebd.Render;
import gebd.camera.Camera;
import gebd.shaders.Shader3D;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

/**
 * Created by alec on 28/07/16.
 */
public class BasicMat4Shader extends Shader3D {
    public BasicMat4Shader(int vertexShaderId, int fragmentShaderId) {
        super(vertexShaderId, fragmentShaderId);
    }

    private int modelRotationMatrixLocation;

    @Override
    public void prepareEntity(Entity entity) {
        setModelPosition(entity.getPosition());
        setModelRotation(entity.getRotation());
        setModelSize(entity.getSize());
    }

    @Override
    protected void setupUniformVariables() {
        modelPosLocation = GLWrapper.glGetUniformLocation(programId, "modelPos");
        modelScaleLocation = GLWrapper.glGetUniformLocation(programId, "modelSize");
        modelRotationLocation = GLWrapper.glGetUniformLocation(programId, "modelRot");
        modelRotationMatrixLocation = GLWrapper.glGetUniformLocation(programId, "rotMatrix");
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

    public void setModelRotation(float qx, float qy, float qz, float qw){
        loadVec4(modelRotationLocation, qx, qy, qz, qw);
    }

    public void loadRotationMatrix(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22){
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.m00 = m00;
        matrix4f.m01 = m01;
        matrix4f.m02 = m02;

        matrix4f.m10 = m10;
        matrix4f.m11 = m11;
        matrix4f.m12 = m12;

        matrix4f.m20 = m20;
        matrix4f.m21 = m21;
        matrix4f.m22 = m22;

        loadMatrix4f(modelRotationMatrixLocation, matrix4f);
    }
}
