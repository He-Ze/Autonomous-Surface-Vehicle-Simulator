package gebd.shaders.shaders;

import blindmystics.util.GLWrapper;
import gebd.Render;
import gebd.shaders.Shader2D;
import javax.vecmath.Vector4f;

public class ColouredText2DShader extends Shader2D {

	/* 2D */
	protected int mixColourLocation;
	protected int mixAmountLocation;
	
	public ColouredText2DShader(int vertexShaderId, int fragmentShaderId) {
		super(vertexShaderId, fragmentShaderId);
	}
	
	@Override
	protected void bindAttributeLocations() {
		// Position information will be attribute 0
		GLWrapper.glBindAttribLocation(programId, 0, "in_Position");

		// Texture information will be attribute 1
		GLWrapper.glBindAttribLocation(programId, 1, "pass_TextureCoord");
	}
	
	@Override
	protected void setupUniformVariables() {
		//Get uniform locations
		
		/* 2D */
		quadPosLocation = getUniformLocation("quadPos");
		quadScaleLocation = getUniformLocation("quadSize");
		quadRotationLocation = getUniformLocation("quadRot");
		screenResolutionLocation = getUniformLocation("screenRes");
		textureCoordLocation_2D = getUniformLocation("textureCoord");
		textureSizeLocation_2D = getUniformLocation("textureSize");
		mixColourLocation = getUniformLocation("mixColour");
		mixAmountLocation = getUniformLocation("mixAmount");
		
	}

	@Override
	public void prepare() {
		setScreenResolution(Render.getWidth(), Render.getHeight());
		setTextureOffset(0, 0);
		setTextureSize(1, 1);
		setQuadRotation(0);
		//loadVec4(mixColourLocation, 0, 1, 0, 1);
		setColour(1, 1, 1, 1); //White.
		setMixAmount(1f);
	}
	
	public void setColour(Vector4f colour){
		loadVec4(mixColourLocation, colour);
	}
	public void setColour(float red, float green, float blue, float alpha){
		loadVec4(mixColourLocation, red, green, blue, alpha);
	}

	public void setMixAmount(float amount){
		loadFloat(mixAmountLocation, amount);
	}
	

}
