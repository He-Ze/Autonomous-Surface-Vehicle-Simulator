package renderables.r2D.text;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import loader.LoadedObject;
import loader.LoadedObjectAbstract;
import loader.LoadedObjectHandler;
import javax.vecmath.Vector2f;

import renderables.texture.TextureHandler;
import renderables.texture.TextureInfo;
import renderables.texture.generated.GeneratedTextureInfo;

/**
 * An Object used to store all the information about ImageFont. An Image Font is a texture
 * containing all characters in a font. You can render part of the texture to render a single
 * character.
 * 
 * @author Peter Smith 43180543
 */
public class ImageFont extends LoadedObjectAbstract {

  // The texture containing all characters, stored on the graphics card.
  protected TextureInfo texture;

  // The maximum width and height any 1 character can be.
  protected int maxCharImgWidth = -1;
  protected int maxCharImgHeight = -1;

  // The width of each character side by side.
  protected int flatRepresentationWidth = -1;

  // The width and height of the texture
  protected int totalWidth = -1;
  protected int totalHeight = -1;

  //The name of the font.
  protected String fontName;
  //PLAIN, ITALIC, BOLD
  protected int fontStyle;
  
  // The Font that corresponds to the ImageFont.
  protected Font font;
  // The metrics surrounding the font, used to determine sizing
  protected FontMetrics fontMetrics;

  // The entire set of all characters available.
  protected StringBuilder askiiCharSet = new StringBuilder();
  // An array that is used to map characters to indexes in a lookup table.
  protected int[] charImageMapping;

  // An array of Images, one for each character.
  protected BufferedImage[] charImages;
  // The number of characters available.
  protected int numChars = -1;

  // The buffer containing all the raw data for the texture of all characters.
  protected ByteBuffer combinedTexture;
  protected int textureWidth = -1;
  protected int textureHeight = -1;

  // The x-y offsets for each character in the generated texture.
  private int[] xOffsets;
  private int[] yOffsets;

  // The OpenGl Texture Coordinates for each character.
  protected float[] uMins;
  protected float[] vMins;
  protected float[] uMaxs;
  protected float[] vMaxs;

  // The sizes of each character.
  protected float widths[];
  protected float heights[];
  protected Vector2f characterSizes[];


  private LoadedObjectHandler.LoadStatus currentStatus;

  /**
   * Default constructor. Nothing is here as this object is generated procedurally by the
   * TextTextureGenerator.
   */
  ImageFont(String fontName, int fontStyle) {
	  this.fontName = fontName;
	  this.fontStyle = fontStyle;
  }



  @Override
  public LoadedObjectHandler.LoadStage[] stagesToPerform(){
    return new LoadedObjectHandler.LoadStage[] {
            LoadedObjectHandler.LoadStage.LOAD_DATA_FROM_FILE,
            LoadedObjectHandler.LoadStage.HANDLE_RAW_DATA,
//				LoadStage.LOAD_DEPENDENCIES,
    };
  }

  @Override
  public LoadedObjectHandler.LoadStatus getLoadStatus() {
    return currentStatus;
  }

  @Override
  public void setLoadStatus(LoadedObjectHandler.LoadStatus newLoadStatus) {
    this.currentStatus = newLoadStatus;
  }


  @Override
  public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
    //This may be implemented in the future when loading true-type text files.
  }

  @Override
  public void handleRawData(LoadedObjectHandler<?> handler) {
    TextTextureGenerator.generateTextTexture(this, fontName, fontStyle, ImageFontHandler.DEFAULT_FONT_TEXTURE_SIZE);
  }

  @Override
  public void loadDependencies(LoadedObjectHandler<?> handler) {
    //Not called, no dependencies.
  }

  @Override
  public void completeLoad(LoadedObjectHandler<?> handler) {
    String uniqueId = getFont().getFontName() + ":" + getFont().getStyle() + ":" + getFont().getSize();
    TextureInfo imageFontTexture = new GeneratedTextureInfo(uniqueId, TextureHandler.DEFAULT_TEXTURE_UNIT,
            getCombinedTexture(), getTextureWidth(), getTextureHeight());
    setTexture(imageFontTexture);
  }

  /**
   * Sets the size of the largest character height and width.
   * 
   * @param maxCharImgWidth
   * @param maxCharImgHeight
   */
  public void setMaxCharTextureSizes(int maxCharImgWidth, int maxCharImgHeight) {
    this.maxCharImgWidth = maxCharImgWidth;
    this.maxCharImgHeight = maxCharImgHeight;
  }

  /**
   * Sets the size of the texture, used for generating the buffer.
   * 
   * @param totalWidth
   * @param totalHeight
   */
  public void setTextureSize(int totalWidth, int totalHeight) {
    this.totalWidth = totalWidth;
    this.totalHeight = totalHeight;
  }

  /**
   * Sets the size of the sum of the widths of all characters.
   * 
   * @param totalWidth
   */
  public void setFlatWidth(int totalWidth) {
    this.flatRepresentationWidth = totalWidth;
  }

  /**
   * @param font - Sets the font associated with the ImageFont.
   */
  public void setFont(Font font) {
    this.font = font;
  }

  /**
   * @param fontMetrics - Sets the font metrics associated with the font.
   */
  public void setFontMetrics(FontMetrics fontMetrics) {
    this.fontMetrics = fontMetrics;
  }

  /**
   * @return int - the sum of the widths of all characters.
   */
  public int getFlatRepresentationWidth() {
    return this.flatRepresentationWidth;
  }

  /**
   * @return int - The largest width of any 1 character.
   */
  public int getMaxCharTexWidth() {
    return this.maxCharImgWidth;
  }

  /**
   * @return int - the largest height of any 1 character.
   */
  public int getMaxCharTexHeight() {
    return this.maxCharImgHeight;
  }

  /**
   * @return - The height of all characters in the font.
   */
  public int getFontHeight() {
    return this.maxCharImgHeight;
  }

  /**
   * @return the Font associated with the ImageFont.
   */
  public Font getFont() {
    return this.font;
  }

  /**
   * @return - The Font Metrics associated with the Font.
   */
  public FontMetrics getFontMetrics() {
    return this.fontMetrics;
  }

  /**
   * @param askiiCharSet - The of all displayable characters.
   */
  public void setCharSetString(StringBuilder askiiCharSet) {
    this.askiiCharSet = askiiCharSet;
  }

  /**
   * @param charImageMapping - sets the mapping from character Id to UV index.
   */
  public void setCharImageMapping(int[] charImageMapping) {
    this.charImageMapping = charImageMapping;
  }

  /**
   * @param charImages - The set of images for each displayable character.
   */
  public void setCharImages(BufferedImage[] charImages) {
    this.charImages = charImages;
    this.numChars = charImages.length;
  }

  /**
   * Sets the combined texture buffer
   * 
   * @param combinedTexture - The buffer information
   * @param textureWidth - The width of the texture.
   * @param textureHeight - The height of the texture.
   */
  public void setCombinedTexture(ByteBuffer combinedTexture, int textureWidth, int textureHeight) {
    this.combinedTexture = combinedTexture;
    this.textureWidth = textureWidth;
    this.textureHeight = textureHeight;
  }


  /**
   * Sets up the Texture coordinates for each character.
   * 
   * @require the size of each array = numChars
   * @param uMins - The bottom left X texture coordinate array.
   * @param vMins - The bottom left Y texture coordinate array.
   * @param uMaxs - The top right X texture coordinate array.
   * @param vMaxs - The top right Y texture coordinate array.
   */
  public void setUVMappings(float[] uMins, float[] vMins, float[] uMaxs, float[] vMaxs) {
    this.uMins = uMins;
    this.vMins = vMins;
    this.uMaxs = uMaxs;
    this.vMaxs = vMaxs;
    calculateCharacterSizes();
  }

  /**
   * @param newTexture - sets the OpenGl texture associated with the Image Font.
   */
  public void setTexture(TextureInfo newTexture) {
    this.texture = newTexture;
  }

  /**
   * Sets the xy offsets within the texture for each character.
   * 
   * @param xOffsets
   * @param yOffsets
   */
  public void setXYOffsets(int[] xOffsets, int[] yOffsets) {
    this.xOffsets = xOffsets;
    this.yOffsets = yOffsets;
  }

  /**
   * @return The set of all displayable characters.
   */
  public StringBuilder getAskiiCharset() {
    return this.askiiCharSet;
  }



  /**
   * @return The set of all images for each displayable character.
   */
  public BufferedImage[] getCharImages() {
    return this.charImages;
  }



  /**
   * @return the buffer information of the texture, to send to OpenGl.
   */
  public ByteBuffer getCombinedTexture() {
    return this.combinedTexture;
  }

  /**
   * @return - The name of the font.
   */
  public String getname() {
    return this.font.getFontName();
  }



  /**
   * @return the texture loaded to the graphics card.
   */
  public TextureInfo getTexture() {
    return this.texture;
  }

  /**
   * @return - The width of the character set.
   */
  public int getTotalWidth() {
    return this.totalWidth;
  }

  /**
   * @return - The height of the character set.
   */
  public int getTotalHeight() {
    return this.totalHeight;
  }

  /**
   * @return - The width of the texture.
   */
  public int getTextureWidth() {
    return this.textureWidth;
  }

  /**
   * @return - The height of the texture.
   */
  public int getTextureHeight() {
    return this.textureHeight;
  }

  /**
   * @return the xOffsets within the texture for each character
   */
  public int[] getXOffsets() {
    return this.xOffsets;
  }

  /**
   * @return the yOffsets within the texture for each character
   */
  public int[] getYOffsets() {
    return this.yOffsets;
  }


  /**
   * For each character, determines the size (in pixels) of each character.
   */
  protected void calculateCharacterSizes() {
    // Initialize the array to the correct size.
    this.widths = new float[this.numChars];
    this.heights = new float[this.numChars];
    this.characterSizes = new Vector2f[this.numChars];

    // Iterate through each character.
    for (int i = 0; i < this.numChars; i++) {
      // Determine the width.
      float uDiff = this.uMaxs[i] - this.uMins[i];
      float width = uDiff * this.totalWidth;
      // All have the same height.
      float height = this.maxCharImgHeight;
      this.widths[i] = width;
      this.heights[i] = height;
      this.characterSizes[i] = new Vector2f(width, height);
    }
  }



  /**
   * @param character - the character to get the mapping for.
   * @return float[] - the UV coordinates to load into OpenGl to render the character.
   */
  public float[] getUVMapping(char character) {
    int characterIndex = (int) character;
    if(characterIndex > charImageMapping.length){
      //Handle characters outside the bounds of what is in the texture.
      characterIndex = 0;
    }

    // Find the charMapping index.
    int charMapping = this.charImageMapping[characterIndex];

    // Load the 4 coordinates into a float array.
    float[] mapping = new float[4];

    float uMin = this.uMins[charMapping];
    float vMin = this.vMins[charMapping];
    float uMax = this.uMaxs[charMapping];
    float vMax = this.vMaxs[charMapping];

    mapping[0] = uMin;
    mapping[1] = vMin;
    mapping[2] = uMax;
    mapping[3] = vMax;

    return mapping;
  }

  /**
   * @param character - The character to find the size for.
   * @return the size of the character.
   */
  public Vector2f getCharacterSize(char character) {
    int charMapping = this.charImageMapping[(int) character];
    return this.characterSizes[charMapping];
  }

  /**
   * @param character - The character to find the width for.
   * @return the width of the character.
   */
  public float getCharacterWidth(char character) {
    int charMapping = this.charImageMapping[(int) character];
    return this.widths[charMapping];
  }

  /**
   * @param character - The character to find the height for.
   * @return the height of the character.
   */
  public float getCharacterHeights(char character) {
    int charMapping = this.charImageMapping[(int) character];
    return this.heights[charMapping];
  }

  /**
   * @param string - a sequence of characters
   * @return - The width of string if it were to be rendered (in pixels)
   */
  public float getStringWidth(String string) {
    float width = 0;
    // Iterate through each character in the string and add its
    // width to the total width.
    for (int charNo = 0; charNo < string.length(); charNo++) {
      char c = string.charAt(charNo);
      width += getCharacterWidth(c);
    }
    return width;
  }


  /**
   * Used for selecting highlighting text within a text box. Take str = "abcd" then will return:
   * [0]a[1]b[2]c[3]d[4] depending on the xOffset.
   * 
   * @param str - the string in which to find the index
   * @param xOffset - The x position relative to start of the string.
   * @return the selectionIndex as described above.
   */
  public int getIndexRelativeToX(String str, float xOffset) {
    float currentX = 0;
    // Iterate through each character.
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      float width = getCharacterWidth(c);
      currentX += width / 2;
      // If the xOffset is less than half way, return the current index.
      if (xOffset < currentX) {
        return i;
      }
      currentX += width / 2;
    }
    // The xOffset is greater than the entire string, return the end.
    return str.length();
  }
  
  /**
   * @return the font name
   */
  public String getFontName(){
	  return this.fontName;
  }
  /**
   * @return the font style PLAIN, ITALIC, BOLD.
   */
  public int getFontStyle(){
	  return this.fontStyle;
  }
  @Override
  public int hashCode(){
	  return generateImageFontHashCode(fontName, fontStyle);
  }
  /**
   * Generates hashCode for the font. 
   * @param fontName
   * @param fontStyle
   * @return
   */
  public static int generateImageFontHashCode(String fontName, int fontStyle){
	  return fontName.hashCode() * 23 + fontStyle;
  }
  @Override
  public boolean equals(Object obj){
	  if(!(obj instanceof ImageFont)){
		  return false;
	  }
	  ImageFont other = (ImageFont) obj;
	  return ((other.fontName.equals(fontName)) && (other.fontStyle == fontStyle));
  }
}
