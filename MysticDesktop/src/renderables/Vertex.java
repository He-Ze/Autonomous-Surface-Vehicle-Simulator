package renderables;

public class Vertex {
	// Vertex data
	private float[] xyzw = new float[] {0f, 0f, 0f, 1f};
	private float[] st = new float[] {0f, 0f};
	private float[] normal = new float[] {0f, 0f, 0f};
	
	// The amount of bytes an element has
	public static final int ELEMENT_BYTES = 4;
	
	// Elements per parameter
	public static final int POSITION_ELEMENT_COUNT = 3;
	public static final int TEXTURE_ELEMENT_COUNT = 2;
	public static final int NORMAL_ELEMENT_COUNT = 3;
	
	// Bytes per parameter
	public static final int POSITION_BYTES_COUNT = POSITION_ELEMENT_COUNT * ELEMENT_BYTES;
	public static final int TEXTURE_BYTE_COUNT = TEXTURE_ELEMENT_COUNT * ELEMENT_BYTES;
	public static final int NORMAL_BYTE_COUNT = NORMAL_ELEMENT_COUNT * ELEMENT_BYTES;
	
	// Byte offsets per parameter
	public static final int POSITION_BYTE_OFFSET = 0;
	public static final int TEXTURE_BYTE_OFFSET = POSITION_BYTE_OFFSET + POSITION_BYTES_COUNT;
	public static final int NORMAL_BYTE_OFFSET = TEXTURE_BYTE_OFFSET + TEXTURE_BYTE_COUNT;
	
	// The amount of elements that a vertex has
	public static final int ELEMENT_COUNT = POSITION_ELEMENT_COUNT + TEXTURE_ELEMENT_COUNT + NORMAL_ELEMENT_COUNT;
	public static final int STRIDE = POSITION_BYTES_COUNT + TEXTURE_BYTE_COUNT + NORMAL_BYTE_COUNT;

	public Vertex(float x, float y, float z, float u, float v, float nx, float ny, float nz) {
		setXYZ(x, y, z);
		setST(u, v);
		setNormal(nx, ny, nz);
	}

	public Vertex() {
	}
	
	public void setXYZ(float x, float y, float z) {
		this.xyzw = new float[] {x, y, z, 1f};
	}
	
	public void setST(float s, float t) {
		this.st = new float[] {s, t};
	}
	
	public void setNormal(float x, float y, float z) {
		this.normal = new float[] {x, y, z};
	}
	
	public float[] getElements() {
		float[] out = new float[Vertex.ELEMENT_COUNT];
		int i = 0;
		
		out[i++] = this.xyzw[0];
		out[i++] = this.xyzw[1];
		out[i++] = this.xyzw[2];
		out[i++] = this.st[0];
		out[i++] = this.st[1];
		out[i++] = this.normal[0];
		out[i++] = this.normal[1];
		out[i++] = this.normal[2];
		
		return out;
	}
	
	public float[] getXYZ() {
		return new float[] {this.xyzw[0], this.xyzw[1], this.xyzw[2]};
	}
	
	public float[] getST() {
		return new float[] {this.st[0], this.st[1]};
	}
	
	public float[] getNormal() {
		return new float[] {this.normal[0], this.normal[1], this.normal[2]};
	}
}