package objUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class QuickImportBoat {

//	private static final String inPath = "Render/res/boat/models/";
//	private static final String outPath = "Render/res/boat/entities/";

//	private static final String inPath = "PhysicsTest/res/phys/models/";
//	private static final String outPath = "PhysicsTest/res/phys/entities/";

	private static final String inPath = "BoatVis/res/boat/models/";
	private static final String outPath = "BoatVis/res/boat/entities/";

	public static void main(String[] args) throws FileNotFoundException {
		File inRoot = new File(inPath);
		File[] files = inRoot.listFiles(), animatedFiles;


		for (File file : files) {
			if (file.isFile()) {
				processObj(file, false);
			} else { //Animated
				animatedFiles = file.listFiles();
				for (File animatedFile : animatedFiles) {
					if (processObj(animatedFile, true)) {
						break;
					}
				}
			}
		}
	}

	private static boolean processObj(File file, boolean animated) {
		String[] split = file.getName().split("\\.");
		String filename = split[0];
		String extension = split[1];

		if (extension.equals("obj")) {
			System.out.println();
			System.out.println("Processing " + file.getName());
			if (!animated) {
				readWrite(file, new File(outPath + filename + ".ent"));
				return true;
			} else {
				if (filename.endsWith("_000001")) {
					filename = filename.replace("_000001", "");
					animatedReadWrite(file.getPath().replace("_000001", "_######"), new File(outPath + filename + ".ent"));
					return true;
				}
			}
		}
		return false;
	}

	private static void readWrite(File inFile, File outFile) {
		if (!outFile.exists()) {
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				System.err.println("Cannot create file: " + outFile.getPath());
				e.printStackTrace();
				return;
			}
		}
		ObjLoad obj = new ObjLoad(inFile);
		EntWrite.write(obj, outFile);
	}

	private static void animatedReadWrite(String inPath, File outFile) {
		AnimatedObjLoad obj = new AnimatedObjLoad(inPath);
		EntWrite.write(obj, outFile);
	}
}
