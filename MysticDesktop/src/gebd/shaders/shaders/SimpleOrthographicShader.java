package gebd.shaders.shaders;

/**
 * Created by alec on 2/12/15.
 */
public class SimpleOrthographicShader extends Textured3DShader {
    protected int uniformScale;

    public SimpleOrthographicShader(int vertexShaderId, int fragmentShaderId) {
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
