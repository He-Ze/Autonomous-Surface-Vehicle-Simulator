package gebd.games.boat.entity;

import javax.vecmath.Vector3f;

/**
 * Created by p3te on 8/11/16.
 */
public class ColourSequenceNode {

    private Vector3f colour;
    private float colourBlendAmount;
    private float duration;

    public ColourSequenceNode(Vector3f colour, float colourBlendAmount, float duration) {
        this.colour = colour;
        this.colourBlendAmount = colourBlendAmount;
        this.duration = duration;
    }

    public Vector3f getColour() {
        return colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }

    public float getColourBlendAmount() {
        return colourBlendAmount;
    }

    public void setColourBlendAmount(float colourBlendAmount) {
        this.colourBlendAmount = colourBlendAmount;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

}
