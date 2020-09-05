package renderables;

import javax.vecmath.Vector3f;

public interface GlObject {
	public void bindVerticies();
	public void setup();
	public void render();
	public void destroy();
	public void incrimentPosition(Vector3f xyz);
	public void setPosition(Vector3f xyz);
	public void incrimentRotation(Vector3f xyz);
	public void setRotation(Vector3f xyz);
}
