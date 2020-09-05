package renderables.r3D.model;

import blindmystics.util.GLWrapper;
import gebd.ModelsetPlus;
import javax.vecmath.Vector3f;

public class ModelsetModel {
	protected String modelName;
	protected String modelENTPath;
	protected PhysicsModelsetModel physics = null;
	protected ModelsetModel graphicalPicking = null;

	protected int pointer;
	protected int length;

	protected Vector3f minimum, maximum, size;

	/**
	 * Default constructor, specifically for the GeneratedModels.
	 */
	protected ModelsetModel(){

	}

	public ModelsetModel(String name, String modelENTPath, int pointer, int length, float[] minimum, float[] maximum) {
		this.modelName = name;
		this.modelENTPath = modelENTPath;
		setPointer(pointer);
		System.out.println(name + " pointer: " + this.pointer + " length: " + length);
		this.length = length;

		this.minimum = new Vector3f(minimum[0], minimum[1], minimum[2]);
		this.maximum = new Vector3f(maximum[0], maximum[1], maximum[2]);
		size = new Vector3f(this.maximum.x - this.minimum.x, this.maximum.y - this.minimum.y, this.maximum.z - this.minimum.z);
	}

	public ModelsetModel(String name, String modelENTPath, int pointer, int length, float[] minimum, float[] maximum, PhysicsModelsetModel physics, ModelsetModel graphics) {
		this(name, modelENTPath, pointer, length, minimum, maximum);
		this.physics = physics;
		this.graphicalPicking = graphics;
	}

	public String getModelName() {
		return modelName;
	}

	public String getModelPath() {
		return modelENTPath;
	}

	public void drawElements() {
		GLWrapper.glDrawElements(GLWrapper.GL_TRIANGLES, length, ModelsetPlus.INDEX_TYPE, pointer);
	}

	public void drawGraphicalPicking() {
		if (graphicalPicking != null) {
			graphicalPicking.drawElements();
		} else if (physics != null) {
			physics.drawElements();
		} else {
			drawElements();
		}
	}

	public PhysicsModelsetModel getPhysicsModel() {
		return physics;
	}

	public boolean hasPhysicsModel() {
		return physics != null;
	}

	public Vector3f getMinimumCoordinates() {
		return minimum;
	}

	public Vector3f getMaximumCoordinates() {
		return maximum;
	}

	public Vector3f getSize() {
		return size;
	}

	public float getLargestSize() {
		if (size.x >= size.y && size.x >= size.z) {
			return size.x;
		} else if (size.y >= size.z) {
			return size.y;
		} else {
			return size.z;
		}
	}

	public float getMinimumSize() {
		if (size.x <= size.y && size.x <= size.z) {
			return size.x;
		} else if(size.y <= size.z){
			return size.y;
		} else {
			return size.z;
		}
	}

	public void setPointer(int pointer) {
		//Converting to pointer in BYTES
		this.pointer = pointer * ModelsetPlus.INDEX_BYTES;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
