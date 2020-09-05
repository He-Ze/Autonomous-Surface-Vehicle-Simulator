package renderables.r2D.composite.scroll;

import blindmystics.input.CurrentInput;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;
import renderables.r2D.composite.BorderedRect;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.simple.SimpleQuad;

/**
 * Created by CaptainPete on 7/30/2016.
 */
public class VerticalScrollBar extends UserInterface {

    private SimpleQuad backgroundQuad;
    private SimpleQuad fullLengthIndicator;
    private BorderedRect scrollPanel;

    private float DEAFULT_PADDING = 5; //px
    private float padding = DEAFULT_PADDING; //px

    private float fullLengthIndicatorWidth = 2; //px
    private float fullLengthIndicatorHeight;

    private float minValue;
    private float maxValue;
    private float visibleRange;
    private float currentValue;
    private float currentVisibleMin;
    private float currentVisibleMax;

    public VerticalScrollBar(float minValue, float maxValue, float visibleRange, float currentValue, Vector2f position, Vector2f size, float rotation) {
        super(position, size, rotation);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.visibleRange = visibleRange;
        this.currentValue = currentValue;

        backgroundQuad = addComponentToTop(new SimpleQuad(new Vector2f(), new Vector2f(), 0f));
        backgroundQuad.setBlend(new Vector4f(0.3f, 0.5f, 0.7f, 1f), 1f);

        fullLengthIndicator = addComponentToTop(new SimpleQuad(new Vector2f(), new Vector2f(), 0f));
        fullLengthIndicator.setBlend(new Vector4f(0.2f, 0.3f, 0.3f, 1f), 1f);

        scrollPanel = addComponentToTop(new BorderedRect(new Vector2f(), new Vector2f(), 0f, 1f));
        scrollPanel.setBackdropColour(new Vector4f(0.6f, 0.8f, 0.8f, 1f), 1f);
        scrollPanel.setLineColour(new Vector4f(0.1f, 0.1f, 0.1f, 1f), 1f);

        updateDisplay();
    }

    private void setCurrentValue(float newCurrentValue) {
        float absoluteVisibleRange = getVisibleRange();

        currentVisibleMin = newCurrentValue - (absoluteVisibleRange / 2f);
        currentVisibleMin = Math.max(currentVisibleMin, minValue);
        currentValue = currentVisibleMin + (absoluteVisibleRange / 2f);

        currentVisibleMax = currentValue + (absoluteVisibleRange / 2f);
        currentVisibleMax = Math.min(currentVisibleMax, maxValue);
        currentValue = currentVisibleMax - (absoluteVisibleRange / 2f);

        currentVisibleMin = currentVisibleMax - absoluteVisibleRange;

        updateDisplay();
    }

    public float getVisibleRange() {
        return Math.min((maxValue - minValue), visibleRange);
    }

    private void updateDisplay() {
        backgroundQuad.setSize(size.x, size.y);

        fullLengthIndicatorHeight = size.y - (padding * 2f);
        fullLengthIndicator.setSize(fullLengthIndicatorWidth, fullLengthIndicatorHeight);

        float scollBarWidth = size.x - (padding * 2f);
        float scollBarHeight = (getVisibleRange() / (maxValue - minValue)) * fullLengthIndicatorHeight;
        scrollPanel.setSize(scollBarWidth, scollBarHeight);

        float minYPosForScrollBar = (-fullLengthIndicatorHeight / 2f);
        float maxYPosForScrollBar = -minYPosForScrollBar;
        float scollBarYPos = minYPosForScrollBar + ((maxYPosForScrollBar - minYPosForScrollBar) * percentageScrolled());
        scrollPanel.setRelativePosition(0, scollBarYPos);
    }

    public float percentageScrolled() {
        return ((currentValue - minValue) / (maxValue - minValue));
    }

    @Override
    public void update(CurrentInput input, float delta) {
        boolean thisReceivedMouse = !input.hasComponentReceivedMouseEvent();
        super.update(input, delta);
        thisReceivedMouse &= input.hasComponentReceivedMouseEvent();
        if (thisReceivedMouse) {
            if (input.isMouseHeld(input.getLeftMouse())) {
                float relativeMouseYPostion = input.getMYpos() - this.absolutePosition.y;
                float minYPosForScrollBar = (-fullLengthIndicatorHeight / 2f);
                float newPercentage = (relativeMouseYPostion - minYPosForScrollBar) / fullLengthIndicatorHeight;
                setCurrentValue((newPercentage * totalRange()) + minValue);
            }
        }
    }

    @Override
    public void render() {
        if (totalRange() > getVisibleRange()) {
            super.render();
        }
    }

    public float totalRange() {
        return (maxValue - minValue);
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
        //Update the current value so it doesn't exceed the minimum, and update the display.
        setCurrentValue(currentValue);
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        //Update the current value so it doesn't exceed the maximum, and update the display.
        setCurrentValue(currentValue);
    }

    public void setVisibleRange(float visibleRange) {
        this.visibleRange = visibleRange;
        //Update the current value so it doesn't exceed the maximum or minimum, and update the display.
        setCurrentValue(currentValue);
    }

    public void increaseMaxValue(float byAmount) {
        setMaxValue(maxValue + byAmount);
    }

    public void increaseMinValue(float byAmount) {
        setMinValue(minValue + byAmount);
    }

    public float getMinValue() {
        return minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public float getCurrentVisibleMin() {
        return currentVisibleMin;
    }

    public float getCurrentVisibleMax() {
        return currentVisibleMax;
    }

}
