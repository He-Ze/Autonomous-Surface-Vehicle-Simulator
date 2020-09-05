package renderables.r3D;

import gebd.shaders.Shader3D;

public interface Renderable3D {
	public void destroy();
	void render(Shader3D shader3d); 
}
