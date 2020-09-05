package gebd.shaders.graphicalPicking;

import gebd.shaders.shaders.Picking3DShader;

/**
 * Created by alec on 3/12/15.
 */
public class ExamplePickingShader extends Picking3DShader {
    protected int uniformScale;


    public ExamplePickingShader(int vertexShaderId, int fragmentShaderId) {
        super(vertexShaderId, fragmentShaderId);
    }

    @Override
    protected void setupUniformVariables() {
        super.setupUniformVariables();
        uniformScale = getUniformLocation("uniformScale");
    }

    public void setUniformScale(float value) {
        loadVec4(uniformScale, value, value, value, 1);
    }

    @Override
    public void prepare() {
        super.prepare();
        setUniformScale(0.1f);
    }
}
