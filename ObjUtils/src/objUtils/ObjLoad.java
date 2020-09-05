package objUtils;
import java.io.File;
import java.util.Scanner;

/**
 * @author Alec Tutin
 * @version 0.9-3
 */
public class ObjLoad {
	private int size = 0;

	private float[] xyz;
	private float[] uv;
	private float[] normals;

	private float[] exportXyz;
	private float[] exportUv;
	private float[] exportNormals;
	
	private short[] indices;
	
	public ObjLoad(File data) { //Ensure obj files include UV information
		if (data.canRead()) {
			load(data);
		} else {
			System.out.println("Cannot read file: " + data.getPath());
		}
	}
	
	private void load(File data) {		
		String currentLine, coordinateData = "", uvData = "", normalData = "", faceData = "";
		String[] split;
		try {
			Scanner scanner = new Scanner(data);
			scanner.useDelimiter("[\n\r\n]");
			
			while(scanner.hasNext()) {
				currentLine = scanner.next();
				
				split = currentLine.split(" ", 2);
				
				switch (split[0]) {
					case ("v"): {
						if (coordinateData.length() != 0) {
							coordinateData = coordinateData.concat(",");
						}
						coordinateData = coordinateData.concat(split[1]);
						break;
					}
					case ("vt"): {
						if (uvData.length() != 0) {
							uvData = uvData.concat(",");
						}
						uvData = uvData.concat(split[1]);
						break;
					}
					case ("vn"): {
						if (normalData.length() != 0) {
							normalData = normalData.concat(",");
						}
						normalData = normalData.concat(split[1]);
						break;
					}
					case ("f"): {
						if (faceData.length() != 0) {
							faceData = faceData.concat(",");
						}
						faceData = faceData.concat(split[1]);
						break;
					}
				}
			}
			
			scanner.close();
		
		} catch (Exception e) {
			System.out.println("File read error: " + data.getPath());
		}
		loadCoordinates(coordinateData);
		loadUv(uvData);
		loadNormals(normalData);
		loadFaces(faceData);

		size = indices.length;

		System.out.println("xyz.length = " + exportXyz.length);
		System.out.println("uv.length = " + exportUv.length);
		System.out.println("normals.length = " + exportNormals.length);
		System.out.println("indices.length = " + indices.length);
	}
	
	private void loadCoordinates(String data) {
		xyz = loadFloats(data);
	}
	
	private void loadUv(String data) {
		uv = loadFloats(data);
	}
	
	private void loadNormals(String data) {
		normals = loadFloats(data);
	}
	
	private void loadFaces(String data) {
		String[] faceStrings, indexStrings, tmp;
		int[] duplicatedIndices;
		if (data != "") {
			faceStrings = data.split(",");
			indexStrings = new String[faceStrings.length * 3];
			for (int a = 0; a < faceStrings.length; a++) {
				tmp = faceStrings[a].split(" ");
				indexStrings[a*3] = tmp[0];
				indexStrings[a*3+1] = tmp[1];
				indexStrings[a*3+2] = tmp[2];
			}
			duplicatedIndices = detectDuplicates(indexStrings);
			prepareExportArrays(indexStrings, duplicatedIndices);
		}
	}
	
	private int[] detectDuplicates(String[] indices) {
		int[] duplicate = new int[indices.length];
		
		for (int a = 0; a < duplicate.length; a++) {
			duplicate[a] = -1;
		}
		
		for (int a = 0; a < indices.length; a++) {
			if (duplicate[a] == -1) {
				for (int b = a + 1; b < indices.length; b++) {
					if (indices[a] == indices[b]) {
						duplicate[b] = a;
					}
				}
			}
		}
		return duplicate;
	}
	
	private void prepareExportArrays(String[] indices, int[] duplicates) {
		int numUniqueIndices = 0, sync = 0;
		String[] valueStrings;
		short[] values;
		
		this.indices = new short[indices.length];
		
		for (int a : duplicates) {
			if (a == -1) {
				numUniqueIndices++;
			}
		}
		
		exportXyz = new float[numUniqueIndices * 3];
		exportUv = new float[numUniqueIndices * 2];
		exportNormals = new float[numUniqueIndices * 3];
		
		for (int a = 0; a < indices.length; a++) {
			if (duplicates[a] == -1) {
				valueStrings = indices[a].split("/");
				values = new short[valueStrings.length];
				for (int b = 0; b < values.length; b++) {
					try {
						values[b] = (short) (Short.parseShort(valueStrings[b]) - 1);
					} catch (NumberFormatException e) {
						throw new OBJDataMissingException("OBJ is malformed!",+ b);
					}
				}
				this.indices[a] = (short) (a - sync);

				exportXyz[(a-sync)*3] = xyz[values[0]*3];
				exportXyz[(a-sync)*3+1] = xyz[values[0]*3+1];
				exportXyz[(a-sync)*3+2] = xyz[values[0]*3+2];
				
				exportUv[(a-sync)*2] = uv[values[1]*2];
				exportUv[(a-sync)*2+1] = 1-uv[values[1]*2+1];
				
				exportNormals[(a-sync)*3] = normals[values[2]*3];
				exportNormals[(a-sync)*3+1] = normals[values[2]*3+1];
				exportNormals[(a-sync)*3+2] = normals[values[2]*3+2];
				
			} else {
				this.indices[a] = (short) (duplicates[a] - sync);
				sync++;
			}
		}
	}
	
	
	
	private float[] loadFloats(String data) {
		if (data != "") {
			String[] separatedData, floatStrings;
			float[] loadedData;
			
			separatedData = data.split(",");
			
			loadedData = new float[separatedData.length * separatedData[0].split(" ").length];
			
			for (int a = 0; a < separatedData.length; a++) {
				floatStrings = separatedData[a].split(" ");
				for (int b = 0; b < floatStrings.length; b++) {
					loadedData[a*floatStrings.length+b] = Float.parseFloat(floatStrings[b]);
				}
			}
			
			return loadedData;
		}
		return new float[0];
	}
	
	public float[] getXyz() {
		return exportXyz;
	}
	
	public float[] getUv() {
		return exportUv;
	}
	
	public float[] getNormals() {
		return exportNormals;
	}
	
	public short[] getIndices() {
		return indices;
	}

	public int size() {
		return size;
	}
}
