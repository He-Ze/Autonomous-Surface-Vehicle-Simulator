package renderables;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import gebd.Render;
import loader.FileLoaderThread;
import loader.LoadedObjectHandler;
import renderables.texture.TextureInfo;

public class TextureInfoGroup {
	private int textureID;
	private int numOfReferences;
	private boolean hasAlpha;
	private ArrayList<ByteBuffer> imageBuffers;
	private ArrayList<Integer> xCoords;
	private ArrayList<Integer> yCoords;
	
	//private ArrayList<TextureInfo> textures;
	
	/*
	 * TODO:
	 * Fix the references thing - I believe that it is not properly implemented!
	 * Probably want to make this either extend the existing one, or use the other one to save loading images multiple times?
	 */
	
	public ByteBuffer buf;
	public int totalWidth = 0;
	public int totalHeight = 0;
	
	public TextureInfoGroup(){
		this.textureID = -1;
	}
	
	public void addTexture(int x, int y, TextureInfo texture){
		imageBuffers.add(texture.getBuf());
		xCoords.add(x);
		yCoords.add(y);
		//process position
		if (texture.hasAlpha()) {
			hasAlpha = true;
		}
	}

	public void initialize() {
		//TODO add all textures together into one big buffa!!!
	}

//	@Override
//	public void loadRawDataFromFile() {
//		
//		try {
//			
//			// Open the PNG file as an InputStream
//			InputStream in = new FileInputStream(getPath(0));
//			// Link the PNG decoder to this stream
//			PNGDecoder decoder = new PNGDecoder(in);
//			//hello!
//			// Get the width and height of the texture
//			totalWidth = decoder.getWidth();
//			totalHeight = decoder.getHeight();
//			hasAlpha = decoder.hasAlpha();
//			
//			// Decode the PNG file in a ByteBuffer
//			buf = ByteBuffer.allocateDirect(
//					4 * decoder.getWidth() * decoder.getHeight());
//			decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);			
//			buf.flip();			
//			
//			in.close();
//		} catch (IOException e) {
//			//TODO - This is a terrible way to handle the failed load of a texture!
//			e.printStackTrace();
//			System.err.println("Could not load file: " + filePaths[0]);
//			System.exit(-1);
//		}
//		Render.instance.addWaitingLoadJob(this);
//	}
//
//	@Override
//	public void handleRawData() {
//		//Won't be called!
//	}
//
//	@Override
//	public void loadDependencies(LoadedObjectHandler<?> handler) {
//		//No dependencies to load, this method will be skipped.
//	}
//
//	@Override
//	public void completeLoad(LoadedObjectHandler<?> handler) {
//		int textureId = Render.instance.GL_GenerateTextureFromPNG(Render.DEFAULT_TEXTURE_UNIT, this);
//		this.setTextureId(textureId);
//		//Remove buf - I think - memory management is weird in java, I just don't want memory leaks.
//		buf.clear();
//		buf = null;
//	}
//	
//	public TextureInfo(String path, int textID){
//		this.filePath = path;
//		this.textureID = textID;
//		this.numOfReferences = 1;
//	}
	
	public void setTextureId(int texId){
		textureID = texId;
	}

	public void incrememntReferences(){
		numOfReferences++;
	}
	
	/* 
	 * Decrememnts the number of references by 1,
	 * if number of references == 0, return true
	 * else, return false
	 */
	public boolean decrememntReferences(){
		numOfReferences--;
		if (numOfReferences == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getTextureId(){
		return textureID;
	}
	
//	public String getPath(int id){
//		return filePaths[id];
//	}
	
	public boolean hasAlpha(){
		return hasAlpha;
	}
	
}

