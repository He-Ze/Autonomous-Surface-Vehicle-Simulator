package gebd.shaders.shaders;

import blindmystics.util.GLWrapper;
import composites.entities.Entity;
import gebd.Render;
import gebd.camera.Camera;
import gebd.light.PointLight;
import gebd.shaders.Shader3D;
import org.lwjgl.opengl.OpenGLException;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import renderables.texture.TextureHandler;
import renderables.texture.TextureInfo;

public class Textured3DShader extends Shader3D {
	protected int lightPositionLocation;
	protected int lightColourLocation;
	protected int lightStrengthLocation;
	protected int textureOffsetLocation_3D;
	protected int ambientLightIntensityLocation;
	protected int clipPlaneLocation;

//	uniform vec3 textureBlendColour;
//	uniform float textureBlendAmount;
	protected int textureBlendColourLocation;
	protected int textureBlendAmountLocation;
	
	public Textured3DShader(int vertexShaderId, int fragmentShaderId) {
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
		// UV information will be attribute 1
		GLWrapper.glBindAttribLocation(programId, 1, "in_TextureCoord");
		// Normal information will be attribute 2
		GLWrapper.glBindAttribLocation(programId, 2, "normal");



	}
	
	@Override
	protected void setupUniformVariables() {
		//Get uniform locations

		// 3D //
		projectionRotationLocation = getUniformLocation("projectionRotationMatrix");
		System.out.println("retrieved projrotmat uniform location: " + projectionRotationLocation);
		viewPosLocation = getUniformLocation("viewPos");
		modelPosLocation = getUniformLocation("modelPos");
		modelScaleLocation = getUniformLocation("modelSize");
		modelRotationLocation = getUniformLocation("modelRot");

		lightPositionLocation = getUniformLocation("lightPosition");
		lightColourLocation = getUniformLocation("lightColour");
		lightStrengthLocation = getUniformLocation("luminosity");

		textureOffsetLocation_3D = getUniformLocation("textureOffset");
		
		ambientLightIntensityLocation = getUniformLocation("ambientLightIntensity");

		textureBlendColourLocation = getUniformLocation("textureBlendColour");
		textureBlendAmountLocation = getUniformLocation("textureBlendAmount");

		clipPlaneLocation = getUniformLocation("clipPlane");
	}

	@Override
	public void prepare() {
		Camera camera = Render.instance.getCamera();
		prepare(camera);

		setClipPlane(Render.instance.getCurrentClipPlane());
	}

	public void prepare(Camera camera) {
		Matrix4f projectionRotation = camera.getProjectionRotationMatrix();
		setProjectionRotationMatrix(projectionRotation);

		Vector3f cameraPos = camera.getPosition();
		setCameraPosition(cameraPos);

		setAmbientLightIntensity(Render.ambientLightIntensity);

		setTextureOffset(new Vector2f(0, 0));
	}

	private Vector3f textureBlendColour = new Vector3f();
	private float textureBlendAmount = 0f;
	private boolean isAtDefaultTextureBlend = false;
	private boolean shouldReturnToDefaultBlend = true;


	private void setTextureBlendColour(Vector3f newTextureBlendColour, float newTextureBlendAmount){
		setTextureBlendColour(newTextureBlendColour.x, newTextureBlendColour.y, newTextureBlendColour.z, newTextureBlendAmount);
	}

	private void setTextureBlendColour(float red, float green, float blue, float newTextureBlendAmount){
		this.textureBlendColour.x = red;
		this.textureBlendColour.y = green;
		this.textureBlendColour.z = blue;

		this.textureBlendAmount = newTextureBlendAmount;

		isAtDefaultTextureBlend = false;
		shouldReturnToDefaultBlend = false;
	}

	
	@Override
	public void prepareEntity(Entity e){
		shouldReturnToDefaultBlend = true;
		Vector3f entityTextureBlendColour = e.getTextureBlendColour();
		if (entityTextureBlendColour != null) {
			setTextureBlendColour(entityTextureBlendColour, e.getTextureBlendAmount());
		}

		if (!isAtDefaultTextureBlend) {
			if (shouldReturnToDefaultBlend) {
				sendToShaderTextureBlendAmount(0f);
				isAtDefaultTextureBlend = true;
			} else {
				sendToShaderTextureBlendColour(textureBlendColour.x, textureBlendColour.y, textureBlendColour.z);
				sendToShaderTextureBlendAmount(textureBlendAmount);
			}
		}



		setTexture(e.getTexture());
		setModelPosition(e.getPosition());
		setModelSize(e.getSize());
		setModelRotation(e);
	}

	public void sendToShaderTextureBlendColour(float red, float green, float blue) {
		loadVec3(textureBlendColourLocation, red, green, blue);
	}

	public void sendToShaderTextureBlendAmount(float blendAmount) {
		loadFloat(textureBlendAmountLocation, blendAmount);
	}
	
	public void setAmbientLightIntensity(float ambientLightIntensity){
		loadFloat(ambientLightIntensityLocation, ambientLightIntensity);
	}

	public void setModelRotation(Entity entity) {
		setModelRotation(entity.getRotation());
	}
	
	public void setTextureOffset(Vector2f textureOffset){
		loadVec2(textureOffsetLocation_3D, textureOffset);
	}

	public void setTexture(TextureInfo texture) {
		TextureHandler.prepareTexture(texture);
	}

	public void setClipPlane(Vector4f clipPlane) {
		setClipPlane(clipPlane.x, clipPlane.y, clipPlane.z, clipPlane.w);
	}

	public void setClipPlane(float a, float b, float c, float d) {
		loadVec4(clipPlaneLocation, a, b, c, d);
	}
	
	public void loadPointLight(PointLight light){
		loadVec3(lightPositionLocation, light.getPosition());
		loadVec3(lightColourLocation, light.getColour());
		loadFloat(lightStrengthLocation, light.getLuminosity());
	}
	
	

}
