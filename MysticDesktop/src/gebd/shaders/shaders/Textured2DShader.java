package gebd.shaders.shaders;

import blindmystics.util.GLWrapper;
import org.lwjgl.opengl.GL20;

import gebd.Render;
import gebd.shaders.Shader2D;


/**
 * The stardard 2D Textured Shader.
 * 
 * @author Peter Smith 43180543
 */
public class Textured2DShader extends Shader2D {

  /**
   * Default constuctor.
   * 
   * @param vertexShaderId
   * @param fragmentShaderId
   */
  public Textured2DShader(int vertexShaderId, int fragmentShaderId) {
    super(vertexShaderId, fragmentShaderId);
  }

  /**
   * Generate the variables that will be sent between the vertex and fragment shaders and from
   * loaded models to the vertex shader.
   */
  @Override
  protected void bindAttributeLocations() {
    // Position information will be attribute 0
    GLWrapper.glBindAttribLocation(this.programId, 0, "in_Position");

    // Texture information will be attribute 1
    GLWrapper.glBindAttribLocation(this.programId, 1, "pass_TextureCoord");
  }

  /**
   * sets up the mapping of GLSL variables to Java variables.
   */
  @Override
  protected void setupUniformVariables() {
    // Get uniform locations
    System.out.println("Performing step: setupUniformVariables");
    /* 2D */
    this.quadPosLocation = getUniformLocation("quadPos");
    this.quadScaleLocation = getUniformLocation("quadSize");
    this.quadRotationLocation = getUniformLocation("quadRotation");
    this.screenResolutionLocation = getUniformLocation("screenRes");
    this.textureCoordLocation_2D = getUniformLocation("textureCoord");
    this.textureSizeLocation_2D = getUniformLocation("textureSize");
    this.mixColourLocation = getUniformLocation("mixColour");
    this.mixAmountLocation = getUniformLocation("mixAmount");
    this.boundaryBottomLeftLocation = getUniformLocation("boundaryBottomLeft");
    this.boundaryTopRightLocation = getUniformLocation("boundaryTopRight");
  }

  /**
   * Called every time the shader is loaded to load the initial variables to the shader.
   */
  @Override
  public void prepare() {
    setScreenResolution(Render.WIDTH, Render.HEIGHT);
    setTextureOffset(0, 0);
    setTextureSize(1, 1);
    resetColour();
    setDisplayBoundary(DEFAULT_DISPLAY_BOUNDARY);
  }
}
