package renderables;

public class ColouredVertexData {
	// Vertex data
	private float[] xy = new float[] {0f, 0f};
	private float[] st = new float[] {0f, 0f};
	private float[] rgba = new float[] {1f, 1f, 1f, 1f};
	
	// The amount of bytes an element has
	public static final int elementBytes = 4;
	
	// Elements per parameter
	public static final int positionElementCount = 2;
	public static final int textureElementCount = 2;
	public static final int colorElementCount = 4;
	
	// Bytes per parameter
	public static final int positionBytesCount = positionElementCount * elementBytes;
	public static final int textureByteCount = textureElementCount * elementBytes;
	public static final int colorByteCount = colorElementCount * elementBytes;
	
	// Byte offsets per parameter
	public static final int positionByteOffset = 0;
	public static final int textureByteOffset = positionByteOffset + positionBytesCount; ;
	public static final int colorByteOffset = textureByteOffset + colorByteCount;
	
	
	// The amount of elements that a vertex has
	public static final int elementCount = positionElementCount + textureElementCount + colorElementCount;
	public static final int stride = positionBytesCount + textureByteCount + colorByteCount;
	
	public void setXY(float x, float y) {
		this.xy = new float[] {x, y};
	}
	
	public void setST(float s, float t) {
		this.st = new float[] {s, t};
	}
	
	public void setRGB(float r, float g, float b) {
		this.setRGBA(r, g, b, 1f);
	}
	
	public void setRGBA(float r, float g, float b, float a) {
		this.rgba = new float[] {r, g, b, a};
	}
	
	public float[] getElements() {
		float[] out = new float[ColouredVertexData.elementCount];
		int i = 0;
		
		out[i++] = this.xy[0];
		out[i++] = this.xy[1];
		
		out[i++] = this.st[0];
		out[i++] = this.st[1];
		
		out[i++] = this.rgba[0];
		out[i++] = this.rgba[1];
		out[i++] = this.rgba[2];
		out[i++] = this.rgba[3];
		
		return out;
	}
	
	public float[] getXY() {
		return new float[] {this.xy[0], this.xy[1]};
	}
	
	public float[] getRGBA() {
		return new float[] {this.rgba[0], this.rgba[1], this.rgba[2], this.rgba[3]};
	}
	
	public float[] getST() {
		return new float[] {this.st[0], this.st[1]};
	}
}

/*
package lwjgl_testing_3D;

public class VertexData {
	// Vertex data
	private float[] xyzw = new float[] {0f, 0f, 0f, 1f};
	private float[] rgba = new float[] {1f, 1f, 1f, 1f};
	private float[] st = new float[] {0f, 0f};
	
	// The amount of bytes an element has
	public static final int ELEMENT_BYTES = 4;
	
	// Elements per parameter
	public static final int POSITION_ELEMENT_COUNT = 4;
	public static final int colorElementCount = 4;
	public static final int TEXTURE_ELEMENT_COUNT = 2;
	
	// Bytes per parameter
	public static final int POSITION_BYTES_COUNT = POSITION_ELEMENT_COUNT * ELEMENT_BYTES;
	public static final int colorByteCount = colorElementCount * ELEMENT_BYTES;
	public static final int TEXTURE_BYTE_COUNT = TEXTURE_ELEMENT_COUNT * ELEMENT_BYTES;
	
	// Byte offsets per parameter
	public static final int POSITION_BYTE_OFFSET = 0;
	public static final int colorByteOffset = POSITION_BYTE_OFFSET + POSITION_BYTES_COUNT;
	public static final int TEXTURE_BYTE_OFFSET = colorByteOffset + colorByteCount;
	
	// The amount of elements that a vertex has
	public static final int elementCount = POSITION_ELEMENT_COUNT +
			colorElementCount + TEXTURE_ELEMENT_COUNT;
	// The size of a vertex in bytes, like in C/C++: sizeof(Vertex)
	public static final int stride = POSITION_BYTES_COUNT + colorByteCount +
			TEXTURE_BYTE_COUNT;
	
	// Setters
	public void setXYZ(float x, float y, float z) {
		this.setXYZW(x, y, z, 1f);
	}
	
	public void setRGB(float r, float g, float b) {
		this.setRGBA(r, g, b, 1f);
	}
	
	public void setST(float s, float t) {
		this.st = new float[] {s, t};
	}
	
	public void setXYZW(float x, float y, float z, float w) {
		this.xyzw = new float[] {x, y, z, w};
	}
	
	public void setRGBA(float r, float g, float b, float a) {
		this.rgba = new float[] {r, g, b, a};
	}
	
	// Getters	
	public float[] getElements() {
		float[] out = new float[VertexData.elementCount];
		int i = 0;
		
		// Insert XYZW elements
		out[i++] = this.xyzw[0];
		out[i++] = this.xyzw[1];
		out[i++] = this.xyzw[2];
		out[i++] = this.xyzw[3];
		// Insert RGBA elements
		out[i++] = this.rgba[0];
		out[i++] = this.rgba[1];
		out[i++] = this.rgba[2];
		out[i++] = this.rgba[3];
		// Insert ST elements
		out[i++] = this.st[0];
		out[i++] = this.st[1];
		
		return out;
	}
	
	public float[] getXYZW() {
		return new float[] {this.xyzw[0], this.xyzw[1], this.xyzw[2], this.xyzw[3]};
	}
	
	public float[] getXYZ() {
		return new float[] {this.xyzw[0], this.xyzw[1], this.xyzw[2]};
	}
	
	public float[] getRGBA() {
		return new float[] {this.rgba[0], this.rgba[1], this.rgba[2], this.rgba[3]};
	}
	
	public float[] getRGB() {
		return new float[] {this.rgba[0], this.rgba[1], this.rgba[2]};
	}
	
	public float[] getST() {
		return new float[] {this.st[0], this.st[1]};
	}
}

*/