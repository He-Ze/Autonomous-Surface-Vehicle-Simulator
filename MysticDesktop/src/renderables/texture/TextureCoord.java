package renderables.texture;

/**
 * A small helper class used to store a rectangle in OpenGl coordinates
 * 
 * @author Peter Smith 43180543
 */
public class TextureCoord {
  // The four coords that make up the rectangle.
  private float bottomX;
  private float bottomY;
  private float topX;
  private float topY;

  /**
   * Default constructor, initializes all variables.
   * 
   * @param bottomX
   * @param bottomY
   * @param topX
   * @param topY
   */
  public TextureCoord(float bottomX, float bottomY, float topX, float topY) {
    this.bottomX = bottomX;
    this.bottomY = bottomY;
    this.topX = topX;
    this.topY = topY;
  }

  /**
   * @return The bottom X value of the rect.
   */
  public float getBottomX() {
    return this.bottomX;
  }

  /**
   * @return The bottom Y value of the rect.
   */
  public float getBottomY() {
    return this.bottomY;
  }

  /**
   * @return The top X value of the rect.
   */
  public float getTopX() {
    return this.topX;
  }

  /**
   * @return The top Y value of the rect.
   */
  public float getTopY() {
    return this.topY;
  }
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(bottomX);
    sb.append("-");
    sb.append(bottomY);
    sb.append("-");
    sb.append(topX);
    sb.append("-");
    sb.append(topY);
    return sb.toString();
  }
}
