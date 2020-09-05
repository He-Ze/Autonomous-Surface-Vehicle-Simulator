package gebd.shaders.shaders.Strongholdaria;

import gebd.Render;
import gebd.shaders.Shader2D;
import org.lwjgl.opengl.GL20;
import javax.vecmath.Vector4f;
import renderables.r2D.DisplayBoundary;


/**
 * The stardard 2D Textured Shader.
 *
 * @author Peter Smith 43180543
 */
public class Textured2DAriaWorldEntity extends Shader2D {

  private static Textured2DAriaWorldEntity instance = null;

  public static Textured2DAriaWorldEntity getInstance() {
    return instance;
  }

  protected int entityColorLocation;

  protected int texture_diffuseLocation;
  protected int secondaryTextureLocation;
  protected int textureBlendLocation;

  /**
   * Default constuctor.
   *
   * @param vertexShaderId
   * @param fragmentShaderId
   */
  public Textured2DAriaWorldEntity(int vertexShaderId, int fragmentShaderId) {
    super(vertexShaderId, fragmentShaderId);
    instance = this;
  }

  /**
   * Generate the variables that will be sent between the vertex and fragment shaders and from
   * loaded models to the vertex shader.
   */
  @Override
  protected void bindAttributeLocations() {
    // Position information will be attribute 0
    GL20.glBindAttribLocation(this.programId, 0, "in_Position");

    // Texture information will be attribute 1
    GL20.glBindAttribLocation(this.programId, 1, "pass_TextureCoord");

    //Lighting colour information will be attribute 2
    GL20.glBindAttribLocation(this.programId, 2, "lighting_Colour");



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
    this.quadRotationLocation = getUniformLocation("quadRot");
    this.screenResolutionLocation = getUniformLocation("screenRes");
    this.textureCoordLocation_2D = getUniformLocation("textureCoord");
    this.textureSizeLocation_2D = getUniformLocation("textureSize");

    this.entityColorLocation = getUniformLocation("entityColour");


    //Texturing.
    texture_diffuseLocation = getUniformLocation("texture_diffuse");
    secondaryTextureLocation = getUniformLocation("secondaryTexture");
    textureBlendLocation = getUniformLocation("textureBlend");
  }

  /**
   * Called every time the shader is loaded to load the initial variables to the shader.
   */
  @Override
  public void prepare() {
    setScreenResolution(Render.WIDTH, Render.HEIGHT);
    setTextureOffset(0, 0);
    setTextureSize(1, 1);
    setColour(1.0f, 1.0f, 0.0f, 1.0f);
    connectTextureUnits();
  }

  public void connectTextureUnits(){
    loadInt(texture_diffuseLocation, 0);
    loadInt(secondaryTextureLocation, 1);
  }

  public void setTextureBlend(float textureBlend){
    loadFloat(textureBlendLocation, textureBlend);
  }

  public void loadEntityColour(float red, float green, float blue) {
    loadEntityColour(red, green, blue, 1.0f);
  }

  public void loadEntityColour(float red, float green, float blue, float alpha) {
    loadVec4(this.entityColorLocation, red, green, blue, alpha);
  }

  @Override
  public void resetColour() {
    //Do nothing.
  }

  @Override
  public void setColour(float red, float green, float blue, float alpha) {
    loadEntityColour(red, green, blue, alpha);
  }

  @Override
  public void setMixAmount(float amount) {
    //Do nothing.
  }

  @Override
  public void setColour(Vector4f colour) {
    setColour(colour.x, colour.y, colour.z, colour.w);
  }

  @Override
  public void setDisplayBoundary(DisplayBoundary newDisplayBoundary) {
    //Do nothing, no display boundary in this shader.
  }
}
