package objUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

/**
 * @author Alec Tutin
 * @version 0.9-3
 */
public class EntWrite {
	private static ObjLoad[] objs;

	private static int totalIndices = 0;
	private static int totalVertices = 0;
	
	private static int[] indices;
	private static float[] xyz;
	private static float[] uv;
	private static float[] normal;

	private static int[] pointers;
	private static int[] lengths;

	private static float[][] minimums;
	private static float[][] maximums;
	
	private static void loadData() {
		totalIndices = 0;
		totalVertices = 0;

		pointers = new int[objs.length];
		lengths = new int[objs.length];

		for (ObjLoad obj : objs) {
			totalIndices += obj.size();
			totalVertices += obj.getXyz().length / 3;
		}

		short[] temp_indices;
		float[] temp_xyz;
		float[] temp_uv;
		float[] temp_normal;
		float[] temp_minXYZ;
		float[] temp_maxXYZ;
		float x, y, z;

		indices = new int[totalIndices];
		xyz = new float[totalVertices * 3];
		uv = new float[totalVertices * 2];
		normal = new float[totalVertices * 3];

		minimums = new float[objs.length][];
		maximums = new float[objs.length][];

		int pointer = 0;

		ObjLoad obj;

		for (int a = 0; a < objs.length; a++) {
			obj = objs[a];
			temp_indices = obj.getIndices();
			temp_xyz = obj.getXyz();
			temp_uv = obj.getUv();
			temp_normal = obj.getNormals();

			temp_minXYZ = new float[3];
			temp_minXYZ[0] = Float.POSITIVE_INFINITY;
			temp_minXYZ[1] = Float.POSITIVE_INFINITY;
			temp_minXYZ[2] = Float.POSITIVE_INFINITY;

			temp_maxXYZ = new float[3];
			temp_maxXYZ[0] = Float.NEGATIVE_INFINITY;
			temp_maxXYZ[1] = Float.NEGATIVE_INFINITY;
			temp_maxXYZ[2] = Float.NEGATIVE_INFINITY;

			for (int b = 0; b < temp_indices.length; b++) {
				indices[pointer + b] = temp_indices[b] + pointer;
			}

			for (int b = 0; b < temp_xyz.length / 3; b++) {
				x = temp_xyz[b * 3];
				y = temp_xyz[b * 3 + 1];
				z = temp_xyz[b * 3 + 2];

				xyz[(pointer + b) * 3] = x;
				xyz[(pointer + b) * 3 + 1] = y;
				xyz[(pointer + b) * 3 + 2] = z;
				uv[(pointer + b) * 2] = temp_uv[b * 2];
				uv[(pointer + b) * 2 + 1] = temp_uv[b * 2 + 1];
				normal[(pointer + b) * 3] = temp_normal[b * 3];
				normal[(pointer + b) * 3 + 1] = temp_normal[b * 3 + 1];
				normal[(pointer + b) * 3 + 2] = temp_normal[b * 3 + 2];

				processMinMax(x, y, z, temp_minXYZ, temp_maxXYZ);
			}

			pointers[a] = pointer;
			pointer += obj.size();
			lengths[a] = obj.size();
			minimums[a] = temp_minXYZ;
			maximums[a] = temp_maxXYZ;
		}
	}

	private static void processMinMax(float x, float y, float z, float[] minXYZ, float[] maxXYZ) {
		if (x < minXYZ[0]) {
			minXYZ[0] = x;
		} else if (x > maxXYZ[0]) {
			maxXYZ[0] = x;
		}

		if (y < minXYZ[1]) {
			minXYZ[1] = y;
		} else if (y > maxXYZ[1]) {
			maxXYZ[1] = y;
		}

		if (z < minXYZ[2]) {
			minXYZ[2] = z;
		} else if (z > maxXYZ[2]) {
			maxXYZ[2] = z;
		}
	}
	
	private static ByteBuffer prepareBuffer() {
		ByteBuffer data = ByteBuffer.allocate((totalVertices * 32) + 4 + (totalIndices * 4) + 4 + (pointers.length * 4) + 4 + (lengths.length * 4) + 4 + minimums.length * 12 + 4 + maximums.length * 12 + 4);

		data.putInt(pointers.length);
		for (int pointer : pointers) {
			data.putInt(pointer);
		}
		data.putInt(lengths.length);
		for (int length : lengths) {
			data.putInt(length);
		}
		data.putInt(totalVertices);
		for (int a = 0; a < totalVertices; a++) {
			data.putFloat(xyz[a*3]);
			data.putFloat(xyz[a*3+1]);
			data.putFloat(xyz[a*3+2]);
			data.putFloat(uv[a*2]);
			data.putFloat(uv[a*2+1]);
			data.putFloat(normal[a*3]);
			data.putFloat(normal[a*3+1]);
			data.putFloat(normal[a*3+2]);
		}
		data.putInt(indices.length);
		for (int a = 0; a < indices.length; a++) {
			data.putInt(indices[a]);
		}
		data.putInt(minimums.length);
		for (float[] minimum : minimums) {
			data.putFloat(minimum[0]);
			data.putFloat(minimum[1]);
			data.putFloat(minimum[2]);
		}
		data.putInt(maximums.length);
		for (float[] maximum : maximums) {
			data.putFloat(maximum[0]);
			data.putFloat(maximum[1]);
			data.putFloat(maximum[2]);
		}
		return data;
	}
	
	public static void write(ObjLoad data, File file) {
		write(new ObjLoad[] {data}, file);
	}

	public static void write(AnimatedObjLoad data, File file) {
		write(data.getObjs(), file);
	}

	private static void write(ObjLoad[] animatedData, File file) {
		ByteBuffer writeBuffer;
		FileOutputStream writer;
		objs = animatedData;
		loadData();
		writeBuffer = prepareBuffer();
		try {
			writer = new FileOutputStream(file);
			writer.write(writeBuffer.array());
			writer.close();
		} catch (Exception e) {
			System.out.println("Cannot write to file: " + file.getPath());
		}
	}
}
