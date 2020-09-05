package gebd.shaders.shaders.Strongholdaria;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import gebd.Render;
import gebd.shaders.Shader2D;


/**
 * The stardard 2D Textured Shader.
 *
 * @author Peter Smith 43180543
 */
public class Textured2DAriaWorld extends Shader2D {

  protected int testColourLocation;

  protected int lightingColorLocation;

  protected IntBuffer lightingBuff = BufferUtils.createIntBuffer(4);

  /**
   * Default constuctor.
   *
   * @param vertexShaderId
   * @param fragmentShaderId
   */
  public Textured2DAriaWorld(int vertexShaderId, int fragmentShaderId) {
    super(vertexShaderId, fragmentShaderId);
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
    this.screenResolutionLocation = getUniformLocation("screenRes");
    this.textureCoordLocation_2D = getUniformLocation("textureCoord");
    this.textureSizeLocation_2D = getUniformLocation("textureSize");
    this.mixColourLocation = getUniformLocation("mixColour");
    this.mixAmountLocation = getUniformLocation("mixAmount");

    this.testColourLocation = getUniformLocation("testColour");

    this.lightingColorLocation = getUniformLocation("lightingColor");
    //GL20.glUniform1(loc4, buffOfIntegers);
    //testColour

    //GL_UNSIGNED_INT_2_10_10_10_REV
//  GL20.glBindAttribPointer(this.programId, 1, "pass_TextureCoord");
    //GL20.glVertexAttribPointer(index, 4, GL30.GL_UNSIGNED_INT_5_9_9_9_REV, GL11.GL_TRUE, stride, buffer);

    //GL20.glGetUniformLocation
    //GL20.glSetU

    //return GL20.glGetUniformLocation(programId, variableName);

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
    loadVec4(this.testColourLocation, 1.0f, 1.0f, 0.0f, 1.0f);
    loadLightingColour(-1, -1, -1, -1);
  }


  public void loadLightingColour(int topLeft, int botLeft, int topRight, int botRight){
    lightingBuff.rewind();
    lightingBuff.put(topLeft);
    lightingBuff.put(botLeft);
    lightingBuff.put(topRight);
    lightingBuff.put(botRight);
    lightingBuff.rewind();
    GL20.glUniform1(lightingColorLocation, lightingBuff);
  }



}
