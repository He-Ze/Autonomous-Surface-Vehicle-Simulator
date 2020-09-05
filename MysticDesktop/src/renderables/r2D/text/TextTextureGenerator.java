package renderables.r2D.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import javax.vecmath.Vector2f;

import renderables.texture.TextureHandler;
import renderables.texture.TextureInfo;
import renderables.texture.generated.GeneratedTextureInfo;

/**
 * A helper class used for the calculations and buffer manipulations used to created an ImageFont
 * from a font.
 * 
 * @author Peter Smith 43180543
 */
public class TextTextureGenerator {

	/**
	 * Called to create an ImageFont from a font.
	 * 
	 * @return - The ImageFont generated
	 */
	static void generateTextTexture(ImageFont imageFont, String fontName, int fontStyle, int defaultFontTextureSize) {
		int fontSize = determineLargestFontSize(fontName, fontStyle, defaultFontTextureSize, defaultFontTextureSize);
		Font font = new Font(fontName, fontStyle, fontSize);
		imageFont.setFont(font);
		generateTextTexture(imageFont);
	}
	private static ImageFont generateTextTexture(ImageFont imageFont) {

		// get the font.
		Font font = imageFont.getFont();

		/*
		 * Because font metrics is based on a graphics context, we need to create a small, temporary
		 * image so we can ascertain the width and height of the final image
		 */
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setFont(font);
		FontMetrics fontMetrics = g2d.getFontMetrics();
		g2d.dispose();
		imageFont.setFontMetrics(fontMetrics);


		StringBuilder askiiCharSet = new StringBuilder();

		int unableToDisplayIndex = -1;
		int currentIndex = 0;

		int[] charImageMapping = new int[256];

		for (int i = 0; i < 256; i++) {
			char c = (char) i;
			if (!font.canDisplay(c)) {
				// Unable to display the character within this font.
				if (unableToDisplayIndex == -1) {
					// The first time we encounter this, it will give us a default
					// image to display. Each subsequent character
					// That cannot be displayed will be set to this image.
					unableToDisplayIndex = currentIndex;
				} else {
					// Set it to be the default missing character.
					charImageMapping[i] = unableToDisplayIndex;
					continue;
				}
			}
			// Append the set of characters that can be displayed
			askiiCharSet.append(c);
			// Add the characterId to the character mapping.
			charImageMapping[i] = currentIndex;
			currentIndex++;
		}

		// Set the set of characters visible and their charactedId mapping.
		imageFont.setCharSetString(askiiCharSet);
		imageFont.setCharImageMapping(charImageMapping);

		// Generate an individual texture for each font.
		generateCharTextures(imageFont);
		// Calculate the dimensions for the OpenGl texture.
		calculateMaxCharDimensions(imageFont);
		// Generate the combined texture buffer for openGl.
		generateCombinedTexture(imageFont);
		// Determine the mapping from character to OpenGl texture mapping.
		setupUVCoords(imageFont);

		// Generate the OpenGl texture and return the new ImageFont.
		/*
    TextureInfo imageFontTexture = TextureGenerator
        .GL_GenerateTextureForImageFont(TextureHandler.DEFAULT_TEXTURE_UNIT, imageFont);
		 */
		return imageFont;
	}

	/**
	 * Creates an image for each displayable character.
	 * 
	 * @param imageFont - The ImageFont to generate the images for.
	 */
	public static void generateCharTextures(ImageFont imageFont) {
		StringBuilder askiiSet = imageFont.getAskiiCharset();
		Font font = imageFont.getFont();
		int numChars = askiiSet.length();

		// Generate an array of images to store each image to.
		BufferedImage[] result = new BufferedImage[numChars];

		// Iterate though each displayable character.
		for (int i = 0; i < numChars; i++) {
			// Setup the Graphics2D engine to generate the image.
			BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = img.createGraphics();
			g2d.setFont(font);
			FontMetrics fontMetrics = g2d.getFontMetrics();
			g2d.dispose();


			// Using the font metrics, determine the width and height of the character.
			String currentChar = "" + askiiSet.charAt(i);
			int width = fontMetrics.stringWidth(currentChar);
			int height = fontMetrics.getHeight();
			// Ensure that we don't have a size 0 image.
			if (width == 0) {
				width = 1;
			}

			// Using the Graphics2D engine, generate the image for the chraracter.
			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			g2d = img.createGraphics();
			// Setup parameters to ensure cleaner character generation.
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
					RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
					RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			g2d.setFont(font);
			fontMetrics = g2d.getFontMetrics();

			// Set the default colour for the text black.
			g2d.setColor(Color.BLACK);
			g2d.drawString(currentChar, 0, fontMetrics.getAscent());
			// close the Graphics2D engine, it is no lonnger needed
			g2d.dispose();

			result[i] = img;
		}

		// setup the character images within ImageFont.
		imageFont.setCharImages(result);
	}



	/**
	 * Used to determine the maximum dimensions for the characters within the font.
	 * 
	 * @param imageFont - The ImageFont to set the values for.
	 */
	public static void calculateMaxCharDimensions(ImageFont imageFont) {
		BufferedImage[] charImages = imageFont.getCharImages();
		int maxWidth = 0;
		int maxHeight = 0;
		int totalWidth = 0;

		// Iterate through each character image and determine its
		// Width and height
		for (int i = 0; i < charImages.length; i++) {
			BufferedImage charImage = charImages[i];

			// Add to the total width.
			totalWidth += charImage.getWidth();

			if (charImage.getWidth() > maxWidth) {
				// The character has the largest width
				maxWidth = charImage.getWidth();
			}


			if (charImage.getHeight() > maxHeight) {
				// The character has the largest height
				maxHeight = charImage.getHeight();
			}
		}

		// Update the imagefont with the new values.
		imageFont.setMaxCharTextureSizes(maxWidth, maxHeight);
		imageFont.setFlatWidth(totalWidth);
	}

	public static int determineLargestFontSize(String fontName, int fontStyle, int texWidth, int texHeight){

		int prevSize = 0;
		int currSize = 64;

		int smallestKnownSizeTooBig = -1;
		int largestKnownSizeTooSmall = -1;

		int bestSize = -1;

		boolean foundValidSize = false;
		
		StringBuilder askiiCharset = null;
		//getAskiiCharset

		while(!foundValidSize){
			Font currentFont = new Font(fontName, fontStyle, currSize);
			if(askiiCharset == null){
				askiiCharset = getAskiiCharset(currentFont);
			}
			
			boolean sizeTooBig = false;
			
//			System.out.println("----");
//			System.out.println("currSize = " + currSize);

			Vector2f minFontDimensions = determineMinTextSizeForFont(currentFont, askiiCharset);
//			System.out.println("minFontDimensions = " + minFontDimensions);
			if((minFontDimensions.x > texWidth) || (minFontDimensions.y > texHeight)){
				sizeTooBig = true;
			}
			
			//O(ln(n)) time complexity bitches!
			//Finds the next size to attempt.
			
			
			
			if(sizeTooBig){
				smallestKnownSizeTooBig = currSize;
				if(largestKnownSizeTooSmall == -1){
					currSize = currSize / 2;
				} else {
					currSize = largestKnownSizeTooSmall + ((smallestKnownSizeTooBig - largestKnownSizeTooSmall) / 2);
				}
			} else {
				//Size is too small.
				largestKnownSizeTooSmall = currSize;
				if(smallestKnownSizeTooBig == -1){
					currSize *= 2;
				} else {
					currSize = largestKnownSizeTooSmall + ((smallestKnownSizeTooBig - largestKnownSizeTooSmall) / 2);
				}
			}
			//---
			if((smallestKnownSizeTooBig - largestKnownSizeTooSmall) == 1){
				foundValidSize = true;
				bestSize = largestKnownSizeTooSmall;
			}
		}

		return bestSize;
	}
	
	
	private static Vector2f determineMinTextSizeForFont(Font font, StringBuilder askiiCharset){
		int numChars = askiiCharset.length();
		
		int totalWidthFlat = 0;
		int heightFlat = 0;

		// Setup the Graphics2D engine to generate the image.
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setFont(font);
		FontMetrics fontMetrics = g2d.getFontMetrics();
		g2d.dispose();
		
		heightFlat = fontMetrics.getHeight();

		for (int i = 0; i < numChars; i++) {
			// Using the font metrics, determine the width and height of the character.
			String currentChar = "" + askiiCharset.charAt(i);
			int width = fontMetrics.stringWidth(currentChar);
			
			
			// Ensure that we don't have a size 0 image.
			if (width == 0) {
				width = 1;
			}
			
			totalWidthFlat += width;
		}
		
		float extraSpace = 1.10f;

		// Determine the required buffer space to hold this information.
		int requiredBufferSize = (int) (totalWidthFlat * heightFlat * extraSpace);

		/*
		 * OpenGl works with textures that have an equal width and height that are both powers of 2. To
		 * determine a suitable size texture, double the width and height until the texture size >=
		 * requiredBufferSize
		 */
		boolean foundValidTextureSize = false;
		int textureWidth = 16;
		int textureHeight = 16;
		while (!foundValidTextureSize) {
			int currentTextureSize = textureWidth * textureHeight;
			if ((currentTextureSize >= requiredBufferSize) && (textureHeight > heightFlat)) {
				// A valid texture size has been found.
				foundValidTextureSize = true;
				break;
			} else {
				// Texture size is too small, double the width and height.
				textureWidth *= 2;
				textureHeight *= 2;
			}
		}
		
		
		return new Vector2f(textureWidth, textureHeight);
	}
	
	
	public static StringBuilder getAskiiCharset(Font font){
		StringBuilder askiiCharSet = new StringBuilder();

		int unableToDisplayIndex = -1;
		int currentIndex = 0;

		for (int i = 0; i < 256; i++) {
			char c = (char) i;
			if (!font.canDisplay(c)) {
				// Unable to display the character within this font.
				if (unableToDisplayIndex == -1) {
					// The first time we encounter this, it will give us a default
					// image to display. Each subsequent character
					// That cannot be displayed will be set to this image.
					unableToDisplayIndex = currentIndex;
				} else {
					continue;
				}
			}
			// Append the set of characters that can be displayed
			askiiCharSet.append(c);
			currentIndex++;
		}
		return askiiCharSet;
	}


	/**
	 * Takes the array of singular character images, and converts them into one large combined texture
	 * 
	 * @param imageFont - The image font to create the buffer for.
	 */
	public static void generateCombinedTexture(ImageFont imageFont) {
		final int BYTES_PER_PIXEL = 4; // RGBA
		BufferedImage[] charImages = imageFont.getCharImages();
		int fontHeight = imageFont.getMaxCharTexHeight();
		int numChars = charImages.length;
		int[] xOffsets = new int[numChars];
		int[] yOffsets = new int[numChars];



		// Get the combined width of all characters.
		int flatRepresentationWidth = imageFont.getFlatRepresentationWidth();
		float extraSpace = 1.20f;

		// Determine the required buffer space to hold this information.
		int requiredBufferSize = (int) (flatRepresentationWidth * fontHeight * extraSpace);

		/*
		 * OpenGl works with textures that have an equal width and height that are both powers of 2. To
		 * determine a suitable size texture, double the width and height until the texture size >=
		 * requiredBufferSize
		 */
		boolean foundValidTextureSize = false;
		int textureWidth = 16;
		int textureHeight = 16;
		while (!foundValidTextureSize) {
			int currentTextureSize = textureWidth * textureHeight;
			if ((currentTextureSize >= requiredBufferSize) && (textureHeight > fontHeight)) {
				// A valid texture size has been found.
				foundValidTextureSize = true;
				break;
			} else {
				// Texture size is too small, double the width and height.
				textureWidth *= 2;
				textureHeight *= 2;
			}
		}

		// set the image font texture size.
		imageFont.setTextureSize(textureWidth, textureHeight);

		// Create a new texture that is the size of the generated texture.
		ByteBuffer texture =
				BufferUtils.createByteBuffer(textureWidth * textureHeight * BYTES_PER_PIXEL);


		int rowNo = 0;
		int currXPos = 1;

		// Iterate though each character.
		for (int i = 0; i < numChars; i++) {

			// Get the imagem and it's image data.
			BufferedImage image = charImages[i];
			int[] pixels = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
			int imgWidth = image.getWidth();

			// determine whether it can fit on the current row.
			if ((currXPos + imgWidth) > textureWidth) {
				// It can't fit on the current row, move it to the next row.
				rowNo++;
				currXPos = 1; //Include a pixel offset between characters.
			}

			// Determine the x and y position within the buffer
			int xOffset = currXPos;
			int yOffset = (rowNo * (fontHeight + 1)) + 1; //Include a pixel offset between characters.
			xOffsets[i] = xOffset;
			yOffsets[i] = yOffset;

			// For each pixel in the image, added it to the
			// correct position in the array.
			for (int y = 0; y < image.getHeight(); y++) {
				int yPosition = y + yOffset;
				int texturePosition = (yPosition * textureHeight) + xOffset;
				// Determine the correct place in the buffer to add the data.
				texture.position(texturePosition * BYTES_PER_PIXEL);
				for (int x = 0; x < image.getWidth(); x++) {

					// Get correct pixel data.
					int pixelIndex = (y * image.getWidth() + x) % pixels.length;
					int pixel = pixels[pixelIndex];

					// Convert the image ARGB to RGBA for OpenGl.
					byte red = (byte) ((pixel >> 16) & 0xFF); // Red component
					byte green = (byte) ((pixel >> 8) & 0xFF); // Green component
					byte blue = (byte) (pixel & 0xFF); // Blue component
					byte alpha = (byte) ((pixel >> 24) & 0xFF); // Alpha component

					// But the colour values into the buffer.
					texture.put(red);
					texture.put(green);
					texture.put(blue);
					texture.put(alpha);
				}
			}
			currXPos += imgWidth + 1;
		}

		// Move buffer pointer to the end and flip buffer.
		texture.position(textureWidth * textureHeight * BYTES_PER_PIXEL);
		texture.flip();

		//		DEBUG:
//		System.out.println("textureWidth = " + textureWidth);
//		System.out.println("textureHeight = " + textureHeight);
//		System.out.println("(textureWidth * textureHeight * BYTES_PER_PIXEL) = " + (textureWidth * textureHeight * BYTES_PER_PIXEL));
//		System.out.println("texture.capacity() = " + texture.capacity());
//		System.out.println("texture.limit() = " + texture.limit());
//		System.out.println("texture.position() = " + texture.position());

		// Store the buffer into the ImageFont.
		imageFont.setCombinedTexture(texture, textureWidth, textureHeight);
		imageFont.setXYOffsets(xOffsets, yOffsets);
	}

	/**
	 * Used to create the mapping from characters to the OpenGl UV texture coordinates.
	 * 
	 * @param imageFont - The ImageFont to generate UVs for.
	 */
	public static void setupUVCoords(ImageFont imageFont) {
		BufferedImage[] charImages = imageFont.getCharImages();
		int numChars = charImages.length;

		float textureWidth = imageFont.getTextureWidth();
		float textureHeight = imageFont.getTextureHeight();

		int[] xOffsets = imageFont.getXOffsets();
		int[] yOffsets = imageFont.getYOffsets();

		// Initialize the arrays.
		float[] uMins = new float[numChars];
		float[] vMins = new float[numChars];
		float[] uMaxs = new float[numChars];
		float[] vMaxs = new float[numChars];

		// Iterate through each character.
		for (int i = 0; i < numChars; i++) {
			BufferedImage charImg = charImages[i];
			float charWidth = charImg.getWidth();
			float charHeight = charImg.getHeight();

			float imgXOffset = xOffsets[i];
			float imgYOffset = yOffsets[i];

			/*
			 * The rectangle of space it takes up within the texture is in pixels. It is converted to the
			 * OpenGl Texture coordinate system.
			 */


			// Bot Left X
			uMins[i] = (imgXOffset / textureWidth);
			// Bot Left Y
			uMaxs[i] = ((imgXOffset + charWidth) / textureWidth);

			// Top Right X
			vMins[i] = (imgYOffset / textureHeight);
			// Top Right Y
			vMaxs[i] = ((imgYOffset + charHeight) / textureHeight);

		}
		imageFont.setUVMappings(uMins, vMins, uMaxs, vMaxs);
	}

	
}
