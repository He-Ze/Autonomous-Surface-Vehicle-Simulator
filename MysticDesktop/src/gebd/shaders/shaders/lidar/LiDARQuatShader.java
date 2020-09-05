package gebd.shaders.shaders.lidar;

import composites.entities.Entity;
import gebd.Render;
import gebd.camera.Camera;
import gebd.light.PointLight;
import gebd.shaders.Shader3D;
import org.lwjgl.opengl.GL20;
import renderables.texture.TextureHandler;
import renderables.texture.TextureInfo;

import javax.vecmath.*;

public class LiDARQuatShader extends LiDAR3DShader {

	Quat4f storedRotation = new Quat4f();

	public LiDARQuatShader(int vertexShaderId, int fragmentShaderId) {
		super(vertexShaderId, fragmentShaderId);
	}

	@Override
	public void prepareEntityRotation(Entity entity) {
		storedRotation.set(entity.getQuatRotation());
		storedRotation.inverse();
		setModelRotation(storedRotation);
	}

	public void setModelRotation(Quat4f rotation) {
		loadVec4(modelRotationLocation, rotation);
	}
}
