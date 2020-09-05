package gebd.shaders.shaders;

import blindmystics.util.GLWrapper;
import org.lwjgl.opengl.GL20;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import composites.entities.Entity;
import gebd.Render;
import gebd.camera.Camera;
import gebd.camera.implementation.TargetedCamera;
import gebd.light.PointLight;
import gebd.shaders.Shader3D;
import renderables.texture.TextureHandler;

public class Textured3DShaderOrthographic extends Shader3D {

	protected int lightPositionLocation;
	protected int lightColourLocation;
	protected int lightStrengthLocation;
	protected int textureOffsetLocation_3D;
	protected int ambientLightIntensityLocation;
	protected int uniformScaleLocation;
	
	public Textured3DShaderOrthographic(int vertexShaderId, int fragmentShaderId) {
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
		GLWrapper.glBindAttribLocation(programId, 0, "in_Position");

		GLWrapper.glBindAttribLocation(programId, 1, "in_TextureCoord");

		GLWrapper.glBindAttribLocation(programId, 2, "normal");

		// Texture information will be attribute 1
		GLWrapper.glBindAttribLocation(programId, 3, "pass_TextureCoord");

		// Texture information will be attribute 1
		GLWrapper.glBindAttribLocation(programId, 4, "surfaceNormal");

		// Texture information will be attribute 1
		GLWrapper.glBindAttribLocation(programId, 5, "toLightVector");

		// Texture information will be attribute 1
		GLWrapper.glBindAttribLocation(programId, 6, "lightDistance");
		
	}
	
	@Override
	protected void setupUniformVariables() {
		//Get uniform locations
		
		// 3D //
		projectionRotationLocation = getUniformLocation("projectionRotationMatrix");
		
		viewPosLocation = getUniformLocation("viewPos");

		modelPosLocation = getUniformLocation("modelPos");
		modelScaleLocation = getUniformLocation("modelSize");
		modelRotationLocation = getUniformLocation("modelRot");

		lightPositionLocation = getUniformLocation("lightPosition");
		lightColourLocation = getUniformLocation("lightColour");
		lightStrengthLocation = getUniformLocation("luminosity");

		textureOffsetLocation_3D = getUniformLocation("textureOffset");
		
		ambientLightIntensityLocation = getUniformLocation("ambientLightIntensity");
		
		uniformScaleLocation = getUniformLocation("uniformScale");
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
		
		setUniformScale(1, 1, 1, 1);
		
		float orthoCameraScale = ((TargetedCamera) camera).getScale();
		setUniformScale(orthoCameraScale, orthoCameraScale, orthoCameraScale, 1); //TODO this should be set automatically by camera
	}
	
	@Override
	public void prepareEntity(Entity e){
		TextureHandler.prepareTexture(e.getTexture());
		setModelPosition(e.getPosition());
		setModelSize(e.getSize());
		setModelRotation(e.getRotation());
	}
	
	
	public void setUniformScale(Vector4f uniformScale){
		loadVec4(uniformScaleLocation, uniformScale);
	}
	public void setUniformScale(float x, float y, float z, float w){
		loadVec4(uniformScaleLocation, x, y, z, w);
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
	
	

}
