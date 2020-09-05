package gebd.shaders;

import blindmystics.util.GLWrapper;
import composites.entities.Entity;
import gebd.Render;
import gebd.camera.Camera;

/**
 * Created by alec on 28/07/16.
 */
public class BasicShader extends Shader3D {
    public BasicShader(int vertexShaderId, int fragmentShaderId) {
        super(vertexShaderId, fragmentShaderId);
    }

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
}
