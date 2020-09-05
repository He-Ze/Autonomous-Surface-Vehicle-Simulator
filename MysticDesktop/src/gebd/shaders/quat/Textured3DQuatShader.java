package gebd.shaders.quat;

import composites.entities.Entity;
import gebd.shaders.shaders.Textured3DShader;
import renderables.r3D.rotation.Quat4fHelper;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class Textured3DQuatShader extends Textured3DShader {

	Quat4f storedRotation = new Quat4f();

	public Textured3DQuatShader(int vertexShaderId, int fragmentShaderId) {
		super(vertexShaderId, fragmentShaderId);
	}

	public void setModelRotation(Quat4f rotation) {
		loadVec4(modelRotationLocation, rotation);
	}

	@Override
	public void setModelRotation(Vector3f modelRotation){
		//TODO - This conversion should probably not be done here.
		Quat4fHelper.toQuat4f(modelRotation, storedRotation);
		setModelRotation(storedRotation);
//		loadVec4(modelRotationLocation, modelRotation, 1);
	}

	@Override
	public void setModelRotation(Entity entity) {
		storedRotation.set(entity.getQuatRotation());
		storedRotation.inverse();
		setModelRotation(storedRotation);
	}
}
