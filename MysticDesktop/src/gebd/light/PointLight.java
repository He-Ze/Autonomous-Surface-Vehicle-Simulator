package gebd.light;

import javax.vecmath.Vector3f;

public class PointLight {
	
	private Vector3f position;
	private Vector3f colour;
	
	private float strength;
	
	public PointLight(Vector3f position, Vector3f colour, float strength) {
		this.position = position;
		this.colour = colour;
		this.strength = strength;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	
	public float getLuminosity() {
		return 1/strength;
	}
}
