package configData;

import java.io.File;

public class test {

	public static void main(String[] args) {
		//File file = new File("/home/alec/Code/ConfigSpec.cfn");
		File file = new File("/home/alec/Code/Java/workspace/ThePrisoner/res/config/BlindDingoesPixels.cfn");
		//File writeFile = new File("/home/alec/Code/ConfigWrite.cfn");
		//File writeFile = new File("/home/alec/Code/ConfigWrite.cfb");
		Config conf = new Config(file);
		//conf.removeSet("another");
		//conf.writeTextFile(writeFile);
		//conf.writeFile(writeFile);
		//conf.printOptions();
		
	}
}
