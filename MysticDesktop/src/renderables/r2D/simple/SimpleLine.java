package renderables.r2D.simple;

import javax.vecmath.Vector2f;

/**
 * Creates a line between two points on the screen.
 * Created by CaptainPete on 24/02/2016.
 */
public class SimpleLine extends SimpleQuad {

    //The start position of the line.
    Vector2f startPosition;
    //The end position of the line.
    Vector2f endPosition;
    //The width of the line.
    float lineWidth;

    /**
     * Default constructor for the line.
     * @param startPosition
     * @param endPosition
     * @param lineWidth
     */
    public SimpleLine(Vector2f startPosition, Vector2f endPosition, float lineWidth){
        super(new Vector2f(), new Vector2f(), 0);
        this.startPosition = new Vector2f(startPosition);
        this.endPosition = new Vector2f(endPosition);
        this.lineWidth = lineWidth;
        calculatePosSizeRotation();
    }

    /**
     * Calculates the position, size and rotation of the line,
     * required when ever one of the two points changes position.
     */
    private void calculatePosSizeRotation(){
        this.relativePosition.x = (endPosition.x + startPosition.x) / 2f;
        this.relativePosition.y = (endPosition.y + startPosition.y) / 2f;

        float xDiff = endPosition.x - startPosition.x;
        float yDiff = endPosition.y - startPosition.y;

        float lineLength = (float) Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
        this.size.x = lineLength;
        this.size.y = lineWidth;

        this.rotation = (float) -Math.atan2(yDiff, xDiff);
    }

    /**
     * Sets a new stating position of the line.
     * @param startX
     * @param startY
     */
    public void setStartPosition(float startX, float startY) {
        startPosition.x = startX;
        startPosition.y = startY;
        calculatePosSizeRotation();
    }

    /**
     * Sets a new end position of the line.
     * @param endX
     * @param endY
     */
    public void setEndPosition(float endX, float endY) {
        endPosition.x = endX;
        endPosition.y = endY;
        calculatePosSizeRotation();
    }
}