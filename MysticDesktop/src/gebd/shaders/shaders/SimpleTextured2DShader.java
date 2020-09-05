package gebd.shaders.shaders;

import gebd.Render;
import gebd.shaders.Shader;
import gebd.shaders.Shader2D;

import org.lwjgl.opengl.GL20;
import javax.vecmath.Vector2f;

public class SimpleTextured2DShader extends Shader2D {
	
	public SimpleTextured2DShader(int vertexShaderId, int fragmentShaderId) {
		super(vertexShaderId, fragmentShaderId);
	}
	
	@Override
	protected void bindAttributeLocations() {
		// Position information will be attribute 0
		GL20.glBindAttribLocation(programId, 0, "in_Position");

		// Texture information will be attribute 1
		GL20.glBindAttribLocation(programId, 1, "pass_TextureCoord");
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
	}

	@Override
	public void prepare() {
		setScreenResolution(Render.getWidth(), Render.getHeight());
		setTextureOffset(0, 0);
		setTextureSize(1, 1);
	}

}
