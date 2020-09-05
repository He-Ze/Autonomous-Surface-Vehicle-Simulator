package objUtils;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;

/**
 * @author Alec Tutin
 * @version 0.9-3
 */
public class EntRead {

	private int[] indices;
	private float[] xyz;
	private float[] uv;
	private float[] normal;

	private int[] pointers;
	private int[] lengths;

	private float[][] minimums;
	private float[][] maximums;
	
	public EntRead(InputStream file, String id) {
		try {
			byte[] dataIn = new byte[file.available()];
			file.read(dataIn);
			ByteBuffer data = ByteBuffer.wrap(dataIn);
			read(data);
		} catch (Exception e) {
			System.err.println("File read error: " + id);
			e.printStackTrace();
		}
	}
	
	private void read(ByteBuffer data) {
		int numOfIndices;

		pointers = new int[data.getInt()];
		for (int a = 0; a < pointers.length; a++) {
			pointers[a] = data.getInt();
		}

		lengths = new int[data.getInt()];
		for (int a = 0; a < lengths.length; a++) {
			lengths[a] = data.getInt();
		}

		int numOfVertices = data.getInt();

		xyz = new float[numOfVertices * 3];
		uv = new float[numOfVertices * 2];
		normal = new float[numOfVertices * 3];

		for (int a = 0; a < numOfVertices; a++) {
			xyz[a*3] = data.getFloat();
			xyz[a*3+1] = data.getFloat();
			xyz[a*3+2] = data.getFloat();
			uv[a*2] = data.getFloat();
			uv[a*2+1] = data.getFloat();
			normal[a*3] = data.getFloat();
			normal[a*3+1] = data.getFloat();
			normal[a*3+2] = data.getFloat();
		}
		
		numOfIndices = data.getInt();
		indices = new int[numOfIndices];
		for (int a = 0; a < numOfIndices; a++) {
			indices[a] = data.getInt();
		}

		float[] tempMin;
		int numMinimums = data.getInt();
		minimums = new float[numMinimums][];
		for (int a = 0; a < numMinimums; a += 3) {
			tempMin = new float[3];

			tempMin[0] = data.getFloat();
			tempMin[1] = data.getFloat();
			tempMin[2] = data.getFloat();

			minimums[a/3] = tempMin;
		}

		float[] tempMax;
		int numMaximums = data.getInt();
		maximums = new float[numMaximums][];
		for (int a = 0; a < numMaximums; a += 3) {
			tempMax = new float[3];

			tempMax[0] = data.getFloat();
			tempMax[1] = data.getFloat();
			tempMax[2] = data.getFloat();

			maximums[a/3] = tempMax;
		}
	}
	
	public int[] getIndices() {
		return indices;
	}
	
	public float[] getXyz() {
		return xyz;
	}
	
	public float[] getUv() {
		return uv;
	}
	
	public float[] getNormals() {
		return normal;
	}

	public int[] getPointers() {
		return pointers;
	}

	public int[] getLengths() {
		return lengths;
	}

	public boolean isAnimated() {
		return pointers.length > 1;
	}

	public float[][] getMaximums() {
		return maximums;
	}

	public float[][] getMinimums() {
		return minimums;
	}
}
