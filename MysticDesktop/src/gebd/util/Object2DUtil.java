package gebd.util;

import gebd.Render;
import renderables.r2D.Renderable2D;

/**
 * Created by CaptainPete on 6/05/2016.
 */
public class Object2DUtil {

    public static boolean isRenderableObjectVisible(Renderable2D renderable2D){
        float sizeX = renderable2D.getSize().x;
        float sizeY = renderable2D.getSize().y;
        float relativeX = renderable2D.getAbsolutePosition().x;
        float relativeY = renderable2D.getAbsolutePosition().y;

        float minRenderX = relativeX - (sizeX / 2f);
        float minRenderY = relativeY - (sizeY / 2f);
        float maxRenderX = relativeX + (sizeX / 2f);
        float maxRenderY = relativeY + (sizeY / 2f);

        //Check whether you can see the object or not.
        if(withinBounds(0, 0, Render.WIDTH, Render.HEIGHT,
                minRenderX, minRenderY, maxRenderX, maxRenderY)){
            return true;
        } else {
            //The object can't be seen.
            return false;
        }
    }

    public static boolean withinBounds(float botLeftXBound, float botLeftYBound, float topRightXBound, float topRightYBound,
                                       float minX, float minY, float maxX, float maxY){
        return !notWithinBounds(botLeftXBound, botLeftYBound, topRightXBound, topRightYBound, minX, minY, maxX, maxY);
    }

    public static boolean notWithinBounds(float botLeftXBound, float botLeftYBound, float topRightXBound, float topRightYBound,
                                          float minX, float minY, float maxX, float maxY){
        return ((maxX < botLeftXBound)
                || (minX > topRightXBound)
                || (maxY < botLeftYBound)
                || (minY > topRightYBound));
    }
}
