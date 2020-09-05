package renderables.r2D.text;

import loader.LoadedObjectHandler;

import java.awt.Font;
import java.util.HashMap;

/**
 * A class designed to hold all the currently loaded image fonts and to load any new image fonts.
 * 
 * @author Peter Smith 43180543
 */
public class ImageFontHandler {


	//The default size for the generated font texture.
	public static final int DEFAULT_FONT_TEXTURE_SIZE = 1024; //1024 X 1024.

	/**
	 * A mapping of Font to loaded image fonts, designed so that fonts aren't loaded twice.
	 */
	protected static HashMap<Integer, LoadedObjectHandler<ImageFont>> loadedImageFonts = new HashMap<Integer, LoadedObjectHandler<ImageFont>>();


	public static ImageFont loadImageFont(String fontName, int fontStyle, LoadedObjectHandler waitingJob) {
		int loadPosition = waitingJob.getDependancyNo();

		Integer imageFontKey = ImageFont.generateImageFontHashCode(fontName, fontStyle);

		LoadedObjectHandler<ImageFont> imageFont = loadedImageFonts.get(imageFontKey);

		if(imageFont != null){
			if(imageFont.isLoaded()){
				//Texture already loaded!
				//System.out.println("Texture already loaded: " + filename);
				waitingJob.notifyLoadedDependancy(loadPosition, imageFont);
			} else {
				//Texture in the process of being loaded!
				//System.out.println("Texture in the process of being loaded: " + filename);
				imageFont.addWaiting(waitingJob, loadPosition);
			}
		} else {
			//Texture doesn't exist at the moment, start loading it!
			//System.out.println("Texture doesn't exist at the moment, start loading it: " + filename);
			imageFont = new LoadedObjectHandler<ImageFont>(new ImageFont(fontName, fontStyle));
			imageFont.addWaiting(waitingJob, loadPosition);
			//System.out.println("textureKey = " + textureKey);
			imageFont.handleLoad();
			loadedImageFonts.put(imageFontKey, imageFont);
		}
		return imageFont.getAttachedObject();
	}

}
