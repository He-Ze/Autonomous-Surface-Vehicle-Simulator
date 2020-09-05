package old.util;

import java.nio.charset.Charset;

public class Charsets {
	public static Charset UTF_8(){
		return Charset.forName("UTF-8");
	}
	public static Charset UTF_16(){
		return Charset.forName("UTF-16");
	}
	public static Charset US_ASCII(){
		return Charset.forName("US-ASCII");
	}
	public static Charset ISO_8859_1(){
		return Charset.forName("ISO-8859-1");
	}
	public static Charset UTF_16_BE(){
		return Charset.forName("UTF-16BE");
	}
	public static Charset UTF_16_LE(){
		return Charset.forName("UTF-16LE");
	}
}