package renderables;

public class Vertex2DSimple {
	// Vertex data
	private float[] xy = new float[] {0f, 0f};

	// The amount of bytes an element has
	public static final int ELEMENT_BYTES = 4;

	// Elements per parameter
	public static final int POSITION_ELEMENT_COUNT = 2;

	// Bytes per parameter
	public static final int POSITION_BYTES_COUNT = POSITION_ELEMENT_COUNT * ELEMENT_BYTES;

	// Byte offsets per parameter
	public static final int POSITION_BYTE_OFFSET = 0;
	public static final int COLOR_BYTE_OFFSET = POSITION_BYTE_OFFSET + POSITION_BYTES_COUNT;

	// The amount of elements that a vertex has
	public static final int ELEMENT_COUNT = POSITION_ELEMENT_COUNT;
	public static final int STRIDE = POSITION_BYTES_COUNT;

	public Vertex2DSimple(){}

	public Vertex2DSimple(float x, float y){
		setXYZ(x, y);
	}

	public void setXYZ(float x, float y) {
		this.xy = new float[] {x, y};
	}

	public float[] getElements() {
		float[] out = new float[Vertex2DSimple.ELEMENT_COUNT];
		int i = 0;

		out[i++] = this.xy[0];
		out[i++] = this.xy[1];

		return out;
	}

	public float[] getXY() {
		return new float[] {this.xy[0], this.xy[1]};
	}
}