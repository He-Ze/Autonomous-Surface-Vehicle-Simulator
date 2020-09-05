package configData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import byteStream.ByteStream;
import byteStream.Charsets;

/**
 * Provides Config, a configuration type with plain text and binary read/write capabilities.
 * @author Alec Tutin
 * @version 0.9-12
 */
public class Config {
	private enum Type {SHORT, STRING, BOOLEAN, INTEGER, FLOAT, DOUBLE};
	
	private File file; //TODO probably don't need this
	//TODO More output on errors, line numbers?
	
	private ArrayList<String> keys = new ArrayList<String>();
	private ArrayList<ArrayList<String>> strings = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<Short>> shorts = new ArrayList<ArrayList<Short>>();
	private ArrayList<ArrayList<Integer>> integers = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Float>> floats = new ArrayList<ArrayList<Float>>();
	private ArrayList<ArrayList<Double>> doubles = new ArrayList<ArrayList<Double>>();
	private ArrayList<Boolean> booleans = new ArrayList<Boolean>();
	private final short START_BINARY_FILE = Short.MAX_VALUE;
	
	private final Charset CHARSET = Charsets.UTF_8();
	
	public Config() {
	}
	
	/**
	 * Constructs a Config type object, a configuration format with plain text read/write capabilities.
	 * @param path The path of the configuration file
	 */
	public Config(File file) {
		this.file = file;
		readFile();
	}
	
	public Config(String path) {
		file = new File(path);
		readFile();
	}
	
	private void readFile() {
		if (file.canRead()) { //TODO for each of these read and write checks, produce useful output
			try {
				if (!readFile(file)) {
					readTextFile(file);
				}
			} catch (Exception e) {
				log("File not found! File: " + file.getPath());
			}
		}
	}
	
	/**
	 * Reads the data from a File into the Config.
	 * @param file A correctly formatted plain text file.
	 */
	private void readTextFile(File file) {
		String line = "";
		try {
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter("[\n\r\n]");
			while (scanner.hasNext()) {
				line += scanner.next();
				if (!line.equals("")) {
					if (!line.substring(0, 1).equals("#")) {
						if (!line.substring(line.length() - 1).equals(",")) {
							line = line.replaceAll(" ?(?<!(?<!\\\\)\\\\)= ?", "=");
							line = line.replaceAll(" ?(?<!(?<!\\\\)\\\\), ?", ",");
							line = line.replace("\t", "");
							readTypeText(line);
							line = "";
						}
					} else {
						line = "";
					}
				}
			}
			scanner.close();
		} catch (Exception e) {
			System.out.println("Read error! File: " + file.getPath());
		}
	}
	
	private void readTypeText(String line) {
		Type type;
		String[] split, options;
		String key;
		short[] shortOptions;
		int[] intOptions;
		float[] floatOptions;
		double[] doubleOptions;
		int optionsLength;
		try {
			split = line.split("(?<!(?<!\\\\)\\\\)@");
			type = Type.values()[Integer.parseInt(split[0])];
			split = split[1].split("(?<!(?<!\\\\)\\\\)=");
			key = split[0].replaceAll("\\\\(?!\\\\)", "");
			options = split[1].split("(?<!(?<!\\\\)\\\\),");
			switch (type) {
				case SHORT:
					optionsLength = options.length;
					shortOptions = new short[optionsLength];
					for (int a = 0; a < optionsLength; a++) {
						shortOptions[a] = Short.parseShort(options[a]);
					}
					addValues(key, shortOptions);
					break;
				case STRING:
					addValues(key, options);
					break;
				case BOOLEAN:
					setBoolean(key, Boolean.parseBoolean(options[0]));
					break;
				case INTEGER:
					optionsLength = options.length;
					intOptions = new int[optionsLength];
					for (int a = 0; a < optionsLength; a++) {
						intOptions[a] = Integer.parseInt(options[a]);
					}
					addValues(key, intOptions);
					break;
				case FLOAT:
					optionsLength = options.length;
					floatOptions = new float[optionsLength];
					for (int a = 0; a < optionsLength; a++) {
						floatOptions[a] = Float.parseFloat(options[a]);
					}
					addValues(key, floatOptions);
					break;
				case DOUBLE:
					optionsLength = options.length;
					doubleOptions = new double[optionsLength];
					for (int a = 0; a < optionsLength; a++) {
						doubleOptions[a] = Double.parseDouble(options[a]);
					}
					addValues(key, doubleOptions);
					break;
				default:
					System.out.println("Type not supported. Type: " + type + " File: " + file.getPath());
					break;
			}
		} catch (Exception e) {
			System.out.println("File malformed! File: " + file.getPath() + "\nLine: " + line);
		}
	}
	
	/**
	 * Writes the Config out to a file at the specified path.
	 * @param path The path and filekey to write to
	 * @return true if the file writes successfully
	 */
	public Boolean writeTextFile(File file) {
		try {
			FileWriter writer = new FileWriter(file);
			String configString = prepareWriteString();
			
			writer.write(configString);
			writer.close();
			return true;
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Prepares a string of the Config for writing to a plain text file.
	 * @return A properly formatted Config string
	 */
	private String prepareWriteString() {
		String configString = "";
		int configSize = keys.size();
		for (int a = 0; a < configSize; a++) {
			configString += prepareShorts(a);
			configString += prepareStrings(a);
			configString += prepareBoolean(a);
			configString += prepareInteger(a);
			configString += prepareFloats(a);
			configString += prepareDouble(a);
		}
		return escapeString(configString);
	}
	
	private String escapeString(String string) {
		string = string.replaceAll("(?<!\\\\)\\\\(?!\\\\)", "\\\\\\\\");
		string = string.replaceAll("(?<!\\\\),(?!\\\\)", "\\\\,");
		string = string.replaceAll("(?<!\\\\)=(?!\\\\)", "\\\\=");
		string = string.replaceAll("(?<!\\\\)@(?!\\\\)", "\\\\@");
		return string;
	}
	
	private String prepareShorts(int index) {
		ArrayList<Short> shortList = shorts.get(index);
		if (shortList.size() > 0) {
			String key = keys.get(index), shortString = "0@" + key + "=";
			
			for (Short value : shortList) {
				shortString += value.toString() + ",";
			}
			shortString = shortString.substring(0, shortString.length() - 1) + "\r\n";
			return shortString;
		}
		return "";
	}
	
	private String prepareInteger(int index) {
		ArrayList<Integer> intList = integers.get(index);
		if (intList.size() > 0) {
			String key = keys.get(index), intString = "3@" + key + "=";
			
			for (Integer value : intList) {
				intString += value.toString() + ",";
			}
			intString = intString.substring(0, intString.length() - 1) + "\r\n";
			return intString;
		}
		return "";
	}
	
	private String prepareFloats(int index) {
		ArrayList<Float> floatList = floats.get(index);
		if (floatList.size() > 0) {
			String key = keys.get(index), floatString = "4@" + key + "=";
			
			for (Float value : floatList) {
				floatString += value.toString() + ",";
			}
			floatString = floatString.substring(0, floatString.length() - 1) + "\r\n";
			return floatString;
		}
		return "";
	}
	
	private String prepareDouble(int index) {
		ArrayList<Double> doubleList = doubles.get(index);
		if (doubleList.size() > 0) {
			String key = keys.get(index), doubleString = "5@" + key + "=";
			
			for (Double value : doubleList) {
				doubleString += value.toString() + ",";
			}
			doubleString = doubleString.substring(0, doubleString.length() - 1) + "\r\n";
			return doubleString;
		}
		return "";
	}
	
	private String prepareStrings(int index) {
		ArrayList<String> stringList = strings.get(index);
		if (stringList.size() > 0) {
			String key = keys.get(index), stringString = "1@" + key + "=";
			for (String string : stringList) {
				stringString += string + ",";
			}
			stringString = stringString.substring(0, stringString.length() - 1) + "\r\n";
			return stringString;
		}
		return "";
	}
	
	private String prepareBoolean(int index) {
		if (booleans.get(index)) {
			return "2@" + keys.get(index) + "=true" + "\r\n";
		}
		return "";
	}
	
	/**
	 * Reads a file into the Config.
	 * @param file A correctly formatted binary file
	 * @return true if the file read
	 */
	public Boolean readFile(File file) {
		if (file.canRead()) {
			try {
				byte[] dataIn = Files.readAllBytes(file.toPath());
				ByteBuffer data = ByteBuffer.allocate(dataIn.length);
				data = ByteBuffer.wrap(dataIn);
				if (data.getShort() == START_BINARY_FILE) {
					readByteArray(dataIn);
					return true;
				}
			} catch (Exception e) {
			}
		}
		return false;
	}
	
	/**
	 * Reads a correctly formatted byte array into the Config.
	 * @param dataIn the correctly formatted byte array
	 */
	public void readByteArray(byte[] dataIn) {
		String key;
		try {
			ByteStream data = new ByteStream(dataIn);
			int configSize = data.ByteBuffer().getShort();
			if (configSize == START_BINARY_FILE) {
				configSize = data.ByteBuffer().getShort();
			}
			for (int a = 0; a < configSize; a++) {
				key = data.getString(CHARSET);
				newSet(key);
				addValues(key, getShorts(data, a));
				addValues(key, getIntegers(data, a));
				addValues(key, getFloats(data, a));
				addValues(key, getDoubles(data, a));
				addValues(key, getStrings(data, a));
				if (data.ByteBuffer().get() == 1) {
					booleans.set(a, true);
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
			log("Read error! File: " + file.getPath());
		}
	}

	/**
	 * Writes the config to a binary file.
	 * @param writeFile The file to write to
	 * @return true if the write succeeded
	 */
	public Boolean writeFile(File file) {
		short configSize = (short) keys.size();
		try {
			FileOutputStream writer = new FileOutputStream(file);
			ByteStream data = new ByteStream(1024); // Arbitrary size, will need to make dynamic in the future
			data.ByteBuffer().putShort(START_BINARY_FILE);
			data.ByteBuffer().putShort(configSize);
			for (int a = 0; a < configSize; a++) {
				data.putString(CHARSET, keys.get(a));
				putShorts(data, a);
				putIntegers(data, a);
				putFloats(data, a);
				putDoubles(data, a);
				putStrings(data, a);
				if (booleans.get(a)) {
					data.ByteBuffer().put((byte) 1);
				} else {
					data.ByteBuffer().put((byte) 0);
				}
			}
			writer.write(data.getData());
			writer.close();
			return true;
		} catch (Exception e) {
			log("Write error! File: " + file.getPath());
		}
		return false;
	}
	
	private void putShorts(ByteStream data, int index) {
		List<Short> optionSet = shorts.get(index);
		short optionSize = (short) optionSet.size();
		data.ByteBuffer().putShort(optionSize);
		for (short value : optionSet) {
			data.ByteBuffer().putShort(value);
		}
	}
	
	private void putIntegers(ByteStream data, int index) {
		List<Integer> optionSet = integers.get(index);
		short optionSize = (short) optionSet.size();
		data.ByteBuffer().putShort(optionSize);
		for (int value : optionSet) {
			data.ByteBuffer().putInt(value);
		}
	}
	
	private void putFloats(ByteStream data, int index) {
		List<Float> optionSet = floats.get(index);
		short optionSize = (short) optionSet.size();
		data.ByteBuffer().putShort(optionSize);
		for (float value : optionSet) {
			data.ByteBuffer().putFloat(value);
		}
	}
	
	private void putDoubles(ByteStream data, int index) {
		List<Double> optionSet = doubles.get(index);
		short optionSize = (short) optionSet.size();
		data.ByteBuffer().putShort(optionSize);
		for (Double value : optionSet) {
			data.ByteBuffer().putDouble(value);
		}
	}
	
	private short[] getShorts(ByteStream data, int index) {
		short optionSize = data.ByteBuffer().getShort();
		short[] optionSet = new short[optionSize];
		for (int a = 0; a < optionSize; a++) {
			optionSet[a] = data.ByteBuffer().getShort();
		}
		return optionSet;
	}
	
	private int[] getIntegers(ByteStream data, int index) {
		short optionSize = data.ByteBuffer().getShort();
		int[] optionSet = new int[optionSize];
		for (int a = 0; a < optionSize; a++) {
			optionSet[a] = data.ByteBuffer().getInt();
		}
		return optionSet;
	}
	
	private float[] getFloats(ByteStream data, int index) {
		short optionSize = data.ByteBuffer().getShort();
		float[] optionSet = new float[optionSize];
		for (int a = 0; a < optionSize; a++) {
			optionSet[a] = data.ByteBuffer().getFloat();
		}
		return optionSet;
	}
	
	private double[] getDoubles(ByteStream data, int index) {
		short optionSize = data.ByteBuffer().getShort();
		double[] optionSet = new double[optionSize];
		for (int a = 0; a < optionSize; a++) {
			optionSet[a] = data.ByteBuffer().getDouble();
		}
		return optionSet;
	}
	
	private void putStrings(ByteStream data, int index) {
		List<String> optionSet = strings.get(index);
		short optionSize = (short) optionSet.size();
		data.ByteBuffer().putShort(optionSize);
		for (String value : optionSet) {
			data.putString(CHARSET, value);
		}
	}
	
	private String[] getStrings(ByteStream data, int index) {
		short optionSize = data.ByteBuffer().getShort();
		String[] optionSet = new String[optionSize];
		for (int a = 0; a < optionSize; a++) {
			optionSet[a] = data.getString(CHARSET);
		}
		return optionSet;
	}
	
	/**
	 * Finds the position in the Config of a specified key.
	 * @param key The key of the key value set
	 * @return Index position of the key, value set
	 */
	public int setExists(String key) {
		int index = -1, configSize = keys.size();
		for (int a = 0; a < configSize; a++) {
			if (keys.get(a).equals(key)) {
				index = a;
				a = keys.size();
			}
		}
		return index;
	}
	
	/**
	 * Retrieves the shorts of the key set.
	 * @param key The key of the set
	 * @return short array containing the values from the set
	 */
	public short[] getShorts(String key) {
		List<Short> values;
		int listSize;
		short[] array = null;
		int index = setExists(key);
		if (index != -1) {
			values = shorts.get(index);
			listSize = values.size();
			array = new short[listSize];
			for (int a = 0; a < listSize; a++) {
				array[a] = values.get(a);
			}
		}
		return array;
	}
	
	public ArrayList<Short> getShortList(String key) {
		int index = setExists(key);
		if (index != -1) {
			return shorts.get(index);
		} else {
			return new ArrayList<Short>();
		}
	}
	
	public int[] getInts(String key) {
		List<Integer> values;
		int listSize;
		int[] array = null;
		int index = setExists(key);
		if (index != -1) {
			values = integers.get(index);
			listSize = values.size();
			array = new int[listSize];
			for (int a = 0; a < listSize; a++) {
				array[a] = values.get(a);
			}
		}
		return array;
	}
	
	public ArrayList<Integer> getIntList(String key) {
		int index = setExists(key);
		if (index != -1) {
			return integers.get(index);
		} else {
			return new ArrayList<Integer>();
		}
	}

	
	public float[] getFloats(String key) {
		List<Float> values;
		int listSize;
		float[] array = null;
		int index = setExists(key);
		if (index != -1) {
			values = floats.get(index);
			listSize = values.size();
			array = new float[listSize];
			for (int a = 0; a < listSize; a++) {
				array[a] = values.get(a);
			}
		}
		return array;
	}
	
	public ArrayList<Float> getFloatList(String key) {
		int index = setExists(key);
		if (index != -1) {
			return floats.get(index);
		} else {
			return new ArrayList<Float>();
		}
	}
	
	public double[] getDoubles(String key) {
		List<Double> values;
		int listSize;
		double[] array = null;
		int index = setExists(key);
		if (index != -1) {
			values = doubles.get(index);
			listSize = values.size();
			array = new double[listSize];
			for (int a = 0; a < listSize; a++) {
				array[a] = values.get(a);
			}
		}
		return array;
	}
	
	public ArrayList<Double> getDoubleList(String key) {
		int index = setExists(key);
		if (index != -1) {
			return doubles.get(index);
		} else {
			return new ArrayList<Double>();
		}
	}
	
	/**
	 * Retrieves the shorts of the key set.
	 * @param key The key of the set
	 * @return String array containing the values from the set
	 */
	public String[] getStrings(String key) {
		List<String> values;
		int listSize;
		String[] array = null;
		int index = setExists(key);
		if (index != -1) {
			values = strings.get(index);
			listSize = values.size();
			array = new String[listSize];
			for (int a = 0; a < listSize; a++) {
				array[a] = values.get(a);
			}
		}
		return array;
	}
	
	public ArrayList<String> getStringList(String key) {
		int index = setExists(key);
		if (index != -1) {
			return strings.get(index);
		} else {
			return new ArrayList<String>();
		}
	}
	
	public boolean getBoolean(String key) {
		int index = setExists(key);
		if (index != -1) {
			return booleans.get(index);
		}
		return false;
	}
	
	public String[] getKeys() {
		return keys.toArray(new String[0]);
	}
	
	public int newSet(String key) {
		int index = setExists(key);
		if (index == -1) {
			keys.add(key);
			strings.add(new ArrayList<String>());
			shorts.add(new ArrayList<Short>());
			integers.add(new ArrayList<Integer>());
			floats.add(new ArrayList<Float>());
			doubles.add(new ArrayList<Double>());
			booleans.add(false);
			return keys.size() - 1;
		}
		return index;
	}
	
	/**
	 * Removes a key, value set of options from the Config.
	 * @param key The key of the key value set
	 */
	public void removeSet(String key) {
		int index = setExists(key);
		if (index != -1) {
			keys.remove(index);
			strings.remove(index);
			shorts.remove(index);
			integers.remove(index);
			floats.remove(index);
			doubles.remove(index);
			booleans.remove(index);
		}
	}
	
	public void addValues(String key, short... values) {
		List<Short> newShorts;
		int index = newSet(key);
		
		newShorts = shorts.get(index);
		for (short value : values) {
			newShorts.add(value);
		}
	}
	
	public void addValues(String key, int... values) {
		List<Integer> newInts;
		int index = newSet(key);
		
		newInts = integers.get(index);
		for (int value : values) {
			newInts.add(value);
		}
	}
	
	public void addValues(String key, float... values) {
		List<Float> newShorts;
		int index = newSet(key);
		
		newShorts = floats.get(index);
		for (float value : values) {
			newShorts.add(value);
		}
	}
	
	public void addValues(String key, double... values) {
		List<Double> newShorts;
		int index = newSet(key);
		
		newShorts = doubles.get(index);
		for (double value : values) {
			newShorts.add(value);
		}
	}
	
	public void addValues(String key, String... values) {
		List<String> newStrings;
		int index = newSet(key);
		
		newStrings = strings.get(index);
		newStrings.addAll(Arrays.asList(values));
	}
	
	public void setBoolean(String key, boolean bool) {
		int index = newSet(key);
		booleans.set(index, bool);
	}
	
	/**
	 * Prints the contents of the Config - for debugging purposes.
	 */
	public void printOptions() {
		int configSize = keys.size();
		for (int a = 0; a < configSize; a++) {
			System.out.println(keys.get(a));
			System.out.print("\tShorts: ");
			for (short value : shorts.get(a)) {
				System.out.print(value + " ");
			}
			System.out.print("\n\tIntegers: ");
			for (int value : integers.get(a)) {
				System.out.print(value + " ");
			}
			System.out.print("\n\tFloats: ");
			for (float value : floats.get(a)) {
				System.out.print(value + " ");
			}
			System.out.print("\n\tDoubles: ");
			for (double value : doubles.get(a)) {
				System.out.print(value + " ");
			}
			System.out.print("\n\tStrings: ");
			for (String value : strings.get(a)) {
				System.out.print("\"" + value + "\" ");
			}
			System.out.print("\n\tBoolean: " + booleans.get(a));
			System.out.println("\n\n");
		}
	}
	
	/**
	 * Shorthand output to console.
	 * @param message Message to be written to the console
	 */
	private static void log(Object message){
        System.out.println("(" + String.valueOf(message) + ")");
    }
}