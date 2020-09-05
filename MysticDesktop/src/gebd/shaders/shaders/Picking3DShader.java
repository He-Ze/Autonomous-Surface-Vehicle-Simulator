package gebd.shaders.shaders;

import blindmystics.util.GLWrapper;
import composites.entities.Entity;
import gebd.Render;
import gebd.camera.Camera;
import gebd.shaders.Shader3D;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import renderables.texture.TextureHandler;

public class Picking3DShader extends Shader3D {

	protected int textureOffsetLocation_3D;

	public Picking3DShader(int vertexShaderId, int fragmentShaderId) {
		super(vertexShaderId, fragmentShaderId);
	}
	
	@Override
	protected void bindAttributeLocations() {
		/*
			in vec4 in_Position;
			in vec2 in_TextureCoord;
			in vec3 normal;

			out vec2 pass_TextureCoord;
		 */

		// Position information will be attribute 0
		GLWrapper.glBindAttribLocation(programId, 0, "in_Position");

		GLWrapper.glBindAttribLocation(programId, 1, "in_TextureCoord");

		GLWrapper.glBindAttribLocation(programId, 2, "normal");

		// Texture information will be attribute 1
		GLWrapper.glBindAttribLocation(programId, 3, "pass_TextureCoord");


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

		textureOffsetLocation_3D = getUniformLocation("textureOffset");

		colourLocation = getUniformLocation("colour");
	}

	@Override
	public void prepare() {
		Camera camera = Render.instance.getCamera();
		
		Matrix4f projectionRotation = camera.getProjectionRotationMatrix();
		setProjectionRotationMatrix(projectionRotation);
		
		Vector3f cameraPos = camera.getPosition();
		setCameraPosition(cameraPos);
		
		setTextureOffset(new Vector2f(0, 0));
	}
	
	@Override
	public void prepareEntity(Entity e){
		TextureHandler.prepareTexture(e.getTexture());
		setModelPosition(e.getPosition());
		setModelSize(e.getSize());
		setModelRotation(e.getRotation());
	}
	
	public void setTextureOffset(Vector2f textureOffset){
		loadVec2(textureOffsetLocation_3D, textureOffset);
	}
}
