package gebd.util;

import gebd.Render;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/**
 * Created by CaptainPete on 6/05/2016.
 */
public class Util3D {

    public static Vector3f translateScreenCoord(float screenX, float screenY){
        return new Vector3f((screenX - (((float) Render.WIDTH)/2)) / (Render.WIDTH/2f), (screenY - (((float) Render.HEIGHT)/2)) / (Render.HEIGHT/2f), 0);
    }

    public static Vector2f getNormalisedDeviceSpace(Vector2f screenPos){
        return getNormalisedDeviceSpace(screenPos.x, screenPos.y);
    }

    public static Vector2f getNormalisedDeviceSpace(float screenX, float screenY){
        return getNormalisedDeviceSpace(screenX, screenY, Render.getWidth(), Render.getHeight());
    }

    public static Vector2f getNormalisedDeviceSpace(Vector2f screenPos, Vector2f renderSize){
        return getNormalisedDeviceSpace(screenPos.x, screenPos.y, renderSize.x, renderSize.y);
    }

    public static Vector2f getNormalisedDeviceSpace(float screenX, float screenY, float renderWidth, float renderHeight){
        float glX = ((screenX * 2f)/ renderWidth) - 1f;
        float glY = ((screenY * 2f)/renderHeight) - 1f;
        return new Vector2f(glX, glY);
    }

    public static Vector2f getScreenSpace(float normalizedX, float normalizedY, float renderWidth, float renderHeight){
        float screenX = ((normalizedX + 1.0f) / 2.0f) * renderWidth;
        float screenY = ((normalizedY + 1.0f) / 2.0f) * renderHeight;
        return new Vector2f(screenX, screenY);
    }
}
