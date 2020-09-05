package renderables;

import blindmystics.input.CurrentInput;
import composites.entities.Entity;
import gebd.camera.Camera;
import org.lwjgl.opengl.GL11;
import renderables.r2D.Quad;
import renderables.r2D.composite.UserInterface;
import renderables.r3D.TransparentEntitySorter;

import javax.vecmath.Vector2f;

import java.util.ArrayList;
import java.util.LinkedList;

import static gebd.Render.HEIGHT;
import static gebd.Render.WIDTH;

/**
 * Created by p3te on 7/11/16.
 */
public abstract class View extends UserInterface {

    LinkedList<Entity> loadingEntites = new LinkedList<>();

    LinkedList<Entity> standardEntities = new LinkedList<>();
    ArrayList<Entity> transparentEntities = new ArrayList<>();

    public View() {
        super(new Vector2f(), new Vector2f(WIDTH, HEIGHT), 0f);
        addNewDependancy(null);
    }

    /**
     * Called when rendering.
     * Will render the 3D components, then the 2D UI components.
     */
    public final void renderCycle(){

        render3D();

        //2D render
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        Quad.bind();
        super.render();

    }

    public abstract void render3D();

    public abstract void logicCycle(CurrentInput currentInput, float delta);

    protected void renderAllLoadedEntites(Camera camera){

        //Start ordering the entites on another thread.
        TransparentEntitySorter.startConcurrentOrdering(camera, transparentEntities);

        //TODO - Render all non-transparent entites.

        //Wait for ordering to complete (If necessary)
        TransparentEntitySorter.waitForSortingToComplete();

        //TODO - Render all transparent entites.

    }

}
