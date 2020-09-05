package gebd.shaders.shaders.lidar;

import composites.entities.Entity;
import gebd.Render;
import gebd.camera.Camera;
import gebd.light.PointLight;
import gebd.shaders.Shader3D;
import org.lwjgl.opengl.GL20;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import renderables.texture.TextureHandler;
import renderables.texture.TextureInfo;

public class LiDAR3DShader extends Shader3D {

	protected int clipPlaneLocation;

	public LiDAR3DShader(int vertexShaderId, int fragmentShaderId) {
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
	}
	
	@Override
	public void prepareEntity(Entity e){
		setModelPosition(e.getPosition());
		setModelSize(e.getSize());
		prepareEntityRotation(e);
	}

	public void prepareEntityRotation(Entity entity) {
		setModelRotation(entity.getRotation());
	}
	
	public void setAmbientLightIntensity(float ambientLightIntensity){
		//Do nothing.
	}
	
	public void setTextureOffset(Vector2f textureOffset){
		//Do nothing.
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
		//Do nothing.
	}
	
	

}
