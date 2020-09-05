package gebd.shaders;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import composites.entities.Entity;
import javax.vecmath.Vector4f;

public abstract class Shader3D extends Shader{
	
	protected int projectionRotationLocation;
	protected int viewPosLocation;
	protected int modelPosLocation;
	protected int modelScaleLocation;
	protected int modelRotationLocation;
	protected int colourLocation;

	public Shader3D(int vertexShaderId, int fragmentShaderId) {
		super(vertexShaderId, fragmentShaderId);
	}

	public abstract void prepareEntity(Entity e);
	
	
	public void setProjectionRotationMatrix(Matrix4f projectionRotationMatrix){
		loadMatrix4f(projectionRotationLocation, projectionRotationMatrix);
	}
	
	public void setCameraPosition(Vector3f cameraPosition){
		loadVec4(viewPosLocation, cameraPosition.x, cameraPosition.y, cameraPosition.z, 0);
	}
	
	public void setModelPosition(Vector3f modelPosition){
		loadVec4(modelPosLocation, modelPosition.x, modelPosition.y, modelPosition.z, 1);
	}
	public void setModelPosition(float x, float y, float z){
		loadVec4(modelPosLocation, x, y, z, 1);
	}
	
	public void setModelSize(Vector3f modelSize){
		loadVec4(modelScaleLocation, modelSize, 1);
	}
	public void setModelSize(float x, float y, float z) {
		loadVec4(modelScaleLocation, x, y, z, 1);
	}
	
	public void setModelRotation(Vector3f modelRotation){
		loadVec4(modelRotationLocation, modelRotation, 1);
	}
	public void setModelRotation(float x, float y, float z){
		loadVec4(modelRotationLocation, x, y, z, 1);
	}

	public void setColour(Vector4f colour) {
		loadVec4(colourLocation, colour);
	}

	public void setColour(float r, float g, float b, float a) {
		loadVec4(colourLocation, r, g, b, a);
	}


}
