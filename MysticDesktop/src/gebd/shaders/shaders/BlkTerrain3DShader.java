package gebd.shaders.shaders;

import java.nio.FloatBuffer;

import gebd.Render;
import gebd.camera.Camera;
import gebd.light.PointLight;
import gebd.shaders.Shader;
import gebd.shaders.Shader3D;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import composites.entities.Entity;

public class BlkTerrain3DShader extends Shader3D {

	
	protected int lightPositionLocation;
	protected int lightColourLocation;
	protected int lightStrengthLocation;
	protected int textureOffsetLocation_3D;
	protected int ambientLightIntensityLocation;
	protected int textureBrightnessLocation;
	
	protected float[] textureBrightness = new float[4];
	protected FloatBuffer floatBuff = BufferUtils.createFloatBuffer(4);
	
	public BlkTerrain3DShader(int vertexShaderId, int fragmentShaderId) {
		super(vertexShaderId, fragmentShaderId);
	}
	
	@Override
	protected void bindAttributeLocations() {


		/*
			in vec4 in_Position;
			in vec2 in_TextureCoord;
			in vec3 normal;

			out vec2 pass_TextureCoord;
			out vec3 surfaceNormal;
			out vec3 toLightVector;
			out float lightDistance;
		 */

		// Position information will be attribute 0
		GL20.glBindAttribLocation(programId, 0, "in_Position");

		GL20.glBindAttribLocation(programId, 1, "in_TextureCoord");

		GL20.glBindAttribLocation(programId, 2, "normal");

		// Texture information will be attribute 1
		GL20.glBindAttribLocation(programId, 3, "pass_TextureCoord");

		// Texture information will be attribute 1
		GL20.glBindAttribLocation(programId, 4, "surfaceNormal");

		// Texture information will be attribute 1
		GL20.glBindAttribLocation(programId, 5, "toLightVector");

		// Texture information will be attribute 1
		GL20.glBindAttribLocation(programId, 6, "lightDistance");
		
		GL20.glBindAttribLocation(programId, 7, "brightness");


	}
	
	@Override
	protected void setupUniformVariables() {
		//Get uniform locations
		
		// 3D //
		projectionRotationLocation = getUniformLocation("projectionRotationMatrix");
		
		viewPosLocation = getUniformLocation("viewPos");

		modelPosLocation = getUniformLocation("modelPos");

		lightPositionLocation = getUniformLocation("lightPosition");
		lightColourLocation = getUniformLocation("lightColour");
		lightStrengthLocation = getUniformLocation("luminosity");

		textureOffsetLocation_3D = getUniformLocation("textureOffset");
		
		ambientLightIntensityLocation = getUniformLocation("ambientLightIntensity");
		
		textureBrightnessLocation = getUniformLocation("textureBrightness");
		//textureBrightnessLocation = getUniformLocation("textureBrightness[2]"); //Can be used to load to a specific index!
		//int loc5 = GL20.glGetUniformLocation(programID, "anArrayOfInts");
		//textureBrightness
		
	}

	@Override
	public void prepare() {
		Camera camera = Render.instance.getCamera();
		
		Matrix4f projectionRotation = camera.getProjectionRotationMatrix();
		setProjectionRotationMatrix(projectionRotation);
		
		Vector3f cameraPos = camera.getPosition();
		setCameraPosition(cameraPos);
		
		setAmbientLightIntensity(Render.ambientLightIntensity);
		
		setTextureOffset(new Vector2f(0, 0));
	}
	
	@Override
	public void prepareEntity(Entity e){
		throw new UnsupportedOperationException();
		/*
		RenderBase.prepareTexture(e.getTexture());
		setModelPosition(e.getPosition());
		setModelSize(e.getSize());
		setModelRotation(e.getRotation());
		*/
	}
	
	public void setTextureBrightness(float b0, float b1, float b2, float b3){
		textureBrightness[0] = b0;
		textureBrightness[1] = b1;
		textureBrightness[2] = b2;
		textureBrightness[3] = b3;
		setTextureBrightness(textureBrightness);
	}
	
	public void setTextureBrightness(float[] brightnesses){
		floatBuff.rewind();
		floatBuff.put(brightnesses);
		floatBuff.rewind();
		loadFloatArray(textureBrightnessLocation, floatBuff);
	}
	
	public void setAmbientLightIntensity(float ambientLightIntensity){
		loadFloat(ambientLightIntensityLocation, ambientLightIntensity);
	}
	
	public void setTextureOffset(Vector2f textureOffset){
		loadVec2(textureOffsetLocation_3D, textureOffset);
	}

	public void loadPointLight(PointLight light){
		loadVec3(lightPositionLocation, light.getPosition());
		loadVec3(lightColourLocation, light.getColour());
		loadFloat(lightStrengthLocation, light.getLuminosity());
	}
	
	@Override
	public void setModelSize(Vector3f modelSize){
		throw new UnsupportedOperationException();
	}
	@Override
	public void setModelSize(float x, float y, float z){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setModelRotation(Vector3f modelRotation){
		throw new UnsupportedOperationException();
	}
	@Override
	public void setModelRotation(float x, float y, float z){
		throw new UnsupportedOperationException();
	}
	
	

}
