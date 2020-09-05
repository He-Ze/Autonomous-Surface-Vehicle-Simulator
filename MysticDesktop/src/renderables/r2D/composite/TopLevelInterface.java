package renderables.r2D.composite;

import blindmystics.input.CurrentInput;
import gebd.Render;
import javax.vecmath.Vector2f;
import renderables.r2D.Renderable2D;
import renderables.r2D.layer.LayerableUtil;

/**
 * Created by CaptainPete on 9/04/2016.
 */
public class TopLevelInterface extends UserInterface {

    private static TopLevelInterface instance = new TopLevelInterface();

    public static TopLevelInterface getInstance(){
        return instance;
    }

    private TopLevelInterface() {
        super(new Vector2f(Render.WIDTH / 2f, Render.HEIGHT / 2f), new Vector2f(Render.WIDTH, Render.HEIGHT), 0);
    }

    public static void addComponentToTopLayer(Renderable2D component) {
        //Remap the LayerHandler to be in a different position.
        LayerableUtil.removeFromLayer(component);
        instance.uiLayerHandler.addToTop(component);
    }

    @Override
    public void update(CurrentInput input, float delta) {
        if (screenResolutionChanged()) {
            setRelativePosition(new Vector2f(Render.WIDTH / 2f, Render.HEIGHT / 2f));
            setSize(new Vector2f(Render.WIDTH, Render.HEIGHT));
        }
        super.update(input, delta);
    }

    /*
    //EXAMPLE USEAGE:
    topLevelQuadPositionTest = rootUI.addComponentToBottom(new UserInterface(new Vector2f(), new Vector2f(), 0));
    topLevelQuadTest = topLevelQuadPositionTest.addComponentToBottom(new SimpleQuad(new Vector2f(0, 0), new Vector2f(40, 40), 0));
    topLevelQuadTest.setBlend(new Vector4f(1f, 1f, 0f, 1f), 1f);
    TopLevelInterface.addComponentToTopLayer(topLevelQuadTest, topLevelQuadPositionTest);
    */
}
