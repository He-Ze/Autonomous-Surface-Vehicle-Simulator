package byteStream;


import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 
 * @author P3TE
 * 
 * An extended version of the ByteBuffer Class including String
 * However, doesn't explicitly extend ByteBuffer because I have encountered
 * Overwrite errors and couldn't be bothered looking into it.
 *
 */
public class ByteStream {
	private ByteBuffer BB;
	
	
	//Byte Encoder Part:
	/**
	 * 
	 * @param capacity int
	 * 
	 * Create a ByteSteam and specify it's maximum capacity
	 */
	public ByteStream(int capacity){
		BB = ByteBuffer.allocate(capacity);
	}
	
	/**
	 * 
	 * @param String to be converted
	 * @param charSet to encode by
	 * 
	 * Takes an input string determines it's length.
	 * Stores the String as a byte array by:
	 * first, storing the length of the String
	 * Secondly, encoding the input String to the specified charset
	 * Thirdly, byte version of the String after the byte version of the int.
	 */
	public void putString(Charset charSet, String string){
		byte[] byteVersionOfString = string.getBytes(charSet);
		short byteArrLength = (short) byteVersionOfString.length;
		BB.putShort(byteArrLength);
		BB.put(byteVersionOfString);
	}
	
	
	/**
	 * 
	 * @param charSet
	 * @return String from array
	 * 
	 * Takes next 4 bytes as an int and uses that int to determine the length of the string
	 * Decodes the next X bytes after the int as a string using the desired charset.
	 */
	public String getString(Charset charSet){
		short stringLength = BB.getShort();
		byte[] byteVersionOfString = new byte[stringLength];
		BB.get(byteVersionOfString, 0, stringLength);
		String returnedValue = new String(byteVersionOfString, charSet);
		return returnedValue;
	}

	/**
	 * 
	 * @return data byte[]
	 * The data within the ByteSteam
	 * It will ONLY retun as much as is stored. 
	 */
	public byte[] getData(){
		int ByteArrayLength = BB.position();
		byte[] BBBArr = new byte[ByteArrayLength];
		BB.position(0);
		BB.get(BBBArr, 0, ByteArrayLength);
		return BBBArr;
	}
	
	//Byte Decoder Part:
	
	
	/**
	 * Create a ByteSteam from a byte array of data.
	 * @param data byte[]
	 */
	public ByteStream(byte[] data){
        BB = ByteBuffer.wrap(data);
    }
	
	//General Use Part:
	/**
	 * 
	 * @return ByteBuffer
	 * 
	 * This give you FULL access of the ByteBuffer.
	 * Although bad practice to give away this, If I were to
	 * extend ByteBuffer, it would be essentially the same thing anyway.
	 * 
	 */
	
	public ByteBuffer ByteBuffer(){
		return BB;
	}
}
