package renderables.r2D.composite.userinterface;

import blindmystics.input.CurrentInput;
import loader.LoadedObjectHandler;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;
import renderables.r2D.Renderable2D;
import renderables.r2D.composite.BorderedRect;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.composite.scroll.VerticalScrollBar;
import renderables.r2D.composite.userinterface.scroll.DropdownMenu;

import java.util.ArrayList;

/**
 * Created by CaptainPete on 7/31/2016.
 */
public class ScrollList extends UserInterface {

    private UserInterface positionParent;
    private BorderedRect backgroundQuad;

    private static final float SCROLL_BAR_WIDTH = 20; //px

    private ArrayList<Renderable2D> components = new ArrayList<>();
    private VerticalScrollBar verticalScrollBar;

    public ScrollList(Vector2f position, Vector2f size, float rotation) {
        super(position, size, rotation);

        backgroundQuad = addComponentToBottom(new BorderedRect(new Vector2f(), size, 0, 2f));
        backgroundQuad.setBackdropColour(new Vector4f(1, 1, 1, 1), 1);
        backgroundQuad.setLineColour(new Vector4f(0, 0, 0, 1), 1);

        positionParent = addComponentToTop(new UserInterface(new Vector2f(), new Vector2f(), 0));

        Vector2f scrollBarPosition = new Vector2f(((size.x - SCROLL_BAR_WIDTH) / 2f), size.y);
        Vector2f scrollBarSize = new Vector2f(SCROLL_BAR_WIDTH, size.y);
        verticalScrollBar = addComponentToTop(new VerticalScrollBar(0, 0, size.y, 0, scrollBarPosition, scrollBarSize, 0));
    }

    public void addComponentToList(Renderable2D renderable2D) {
        components.add(renderable2D);
        positionParent.addComponentToTop(renderable2D);
        float userInterfaceHeight = renderable2D.getSize().y;
        float verticalScrollBarMinVal = verticalScrollBar.getMinValue();
        float relativeYPos = verticalScrollBarMinVal - (userInterfaceHeight / 2f);
        renderable2D.setRelativePosition(0, relativeYPos);
        verticalScrollBar.increaseMinValue(-userInterfaceHeight);
        setHeight(getHeight() + userInterfaceHeight);
    }

    public void remove(int index) {
        if (index >= components.size()) {
            return;
        }

        Renderable2D renderable2D = components.remove(index);
        float componentHeight = renderable2D.getSize().y;
        for (int i = index; i < components.size(); i++) {
            components.get(i).setRelY(components.get(i).getRelY() + componentHeight);
        }

        verticalScrollBar.increaseMinValue(componentHeight);
        setHeight(getHeight() - componentHeight);

        renderable2D.removeAndRemoveAllChildren();
    }

    @Override
    public void completeLoad(LoadedObjectHandler<?> handler) {
        super.completeLoad(handler);
        //uiLayerHandler.moveToTop(verticalScrollBar); // This shouldn't be required!
    }

    @Override
    public void update(CurrentInput input, float delta) {
        super.update(input, delta);
        float positionParentRelX = -(SCROLL_BAR_WIDTH / 2f);
        float positionParentRelY = (size.y / 2f) - verticalScrollBar.getCurrentVisibleMax();
        positionParent.setRelativePosition(positionParentRelX, positionParentRelY);
    }

    @Override
    public void render() {
        super.render();
        //TODO - Consider rendering only what is visible!
    }

    public void setItemListSize(float itemListSize) {
        setWidth(itemListSize + SCROLL_BAR_WIDTH);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        backgroundQuad.setSize(width, height);
        //Update the component positions.
        Vector2f scrollBarPosition = new Vector2f(((size.x - SCROLL_BAR_WIDTH) / 2f), 0);
        verticalScrollBar.setRelativePosition(scrollBarPosition);
        verticalScrollBar.setHeight(size.y);
        verticalScrollBar.setVisibleRange(size.y);
    }

    public BorderedRect getBackgroundQuad() {
        return backgroundQuad;
    }

}
