package renderables;

public class Vertex2D {
	// Vertex data
	private float[] xy = new float[] {0f, 0f};
	private float[] st = new float[] {0f, 0f};
	
	// The amount of bytes an element has
	public static final int elementBytes = 4;
	
	// Elements per parameter
	public static final int positionElementCount = 2;
	public static final int textureElementCount = 2;
	
	// Bytes per parameter
	public static final int positionBytesCount = positionElementCount * elementBytes;
	public static final int textureByteCount = textureElementCount * elementBytes;
	
	// Byte offsets per parameter
	public static final int positionByteOffset = 0;
	public static final int colorByteOffset = positionByteOffset + positionBytesCount;
	public static final int textureByteOffset = colorByteOffset;
	
	// The amount of elements that a vertex has
	public static final int elementCount = positionElementCount + textureElementCount;
	public static final int stride = positionBytesCount + textureByteCount;
	
	public Vertex2D(){}
	
	public Vertex2D(float x, float y, float s, float t){
		setXYZ(x, y);
		setST(s, t);
	}
	
	public void setXYZ(float x, float y) {
		this.xy = new float[] {x, y};
	}
	
	public void setST(float s, float t) {
		this.st = new float[] {s, t};
	}
	
	public float[] getElements() {
		float[] out = new float[Vertex2D.elementCount];
		int i = 0;
		
		out[i++] = this.xy[0];
		out[i++] = this.xy[1];
		out[i++] = this.st[0];
		out[i++] = this.st[1];
		
		return out;
	}
	
	public float[] getXY() {
		return new float[] {this.xy[0], this.xy[1]};
	}
	
	public float[] getST() {
		return new float[] {this.st[0], this.st[1]};
	}
}