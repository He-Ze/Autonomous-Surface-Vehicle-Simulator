package renderables.r2D;

import javax.vecmath.Vector2f;

/**
 * A class for storing the display boundary (in pixels) Nothing can be rendered outside of these
 * boundaries.
 * 
 * @author Peter Smith 43180543
 */
public class DisplayBoundary {
  // Bottom Left of the boundary.
  Vector2f botLeft = new Vector2f(0, 0);
  // Top Right of the boundary.
  Vector2f topRight = new Vector2f(0, 0);

  /**
   * Default constructor.
   * 
   * @param botLeft - Vector2f
   * @param topRight - Vector2f
   */
  public DisplayBoundary(Vector2f botLeft, Vector2f topRight) {
    setBoundary(botLeft, topRight);
  }

  /**
   * Alternative Default Constructor.
   * 
   * @param botLeftX
   * @param botLeftY
   * @param topRightX
   * @param topRightY
   */
  public DisplayBoundary(float botLeftX, float botLeftY, float topRightX, float topRightY) {
    setBoundary(botLeftX, botLeftY, topRightX, topRightY);
  }

  /**
   * Used to change the boundary dimensions and position.
   * 
   * @param botLeftX
   * @param botLeftY
   * @param topRightX
   * @param topRightY
   */
  public void setBoundary(float botLeftX, float botLeftY, float topRightX, float topRightY) {
    this.botLeft.x = botLeftX;
    this.botLeft.y = botLeftY;
    this.topRight.x = topRightX;
    this.topRight.y = topRightY;
  }

  /**
   * Used to change the boundary dimensions and position.
   * 
   * @param newBotLeft
   * @param newTopRight
   */
  public void setBoundary(Vector2f newBotLeft, Vector2f newTopRight) {
    setBoundary(newBotLeft.x, newBotLeft.y, newTopRight.x, newTopRight.y);
  }

  /**
   * @return the bottom left X of the boundary.
   */
  public float getBotLeftX() {
    return this.botLeft.x;
  }

  /**
   * @return the bottom left Y of the boundary.
   */
  public float getBotLeftY() {
    return this.botLeft.y;
  }

  /**
   * @return the top right X of the boundary.
   */
  public float getTopRightX() {
    return this.topRight.x;
  }

  /**
   * @return the top right Y of the boundary.
   */
  public float getTopRightY() {
    return this.topRight.y;
  }

  /**
   * @return bottom left (x,y)
   */
  public Vector2f getBotLeft() {
    return this.botLeft;
  }

  /**
   * @return top right (x,y)
   */
  public Vector2f getTopRight() {
    return this.topRight;
  }

  /**
   * Combines two display boundaries. by taking the area that is common between them.
   * 
   * @param boundary1
   * @param boundary2
   * @return a combination of the two.
   */
  public static DisplayBoundary combine(DisplayBoundary boundary1, DisplayBoundary boundary2) {
    float minX = Math.min(boundary1.getBotLeftX(), boundary2.getBotLeftX());
    float maxX = Math.max(boundary1.getTopRightX(), boundary2.getTopRightX());

    float minY = Math.min(boundary1.getBotLeftY(), boundary2.getBotLeftY());
    float maxY = Math.max(boundary1.getTopRightY(), boundary2.getTopRightY());

    // Add only the area that is common to both.
    Vector2f bottomLeft = new Vector2f(minX, minY);
    Vector2f topRight = new Vector2f(maxX, maxY);

    DisplayBoundary combination = new DisplayBoundary(bottomLeft, topRight);
    return combination;
  }

  /**
   * @param mXpos
   * @param mYpos
   * @return whether an (x,y) position is within the display boundaries or not.
   */
  public boolean isWithinBounds(int mXpos, int mYpos) {
    return ((mXpos >= this.botLeft.x) && ((mXpos <= this.topRight.x)) && (mYpos >= this.botLeft.y)
        && ((mYpos <= this.topRight.y)));
  }
}
