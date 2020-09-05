package renderables.texture.generated;

import renderables.texture.TextureInfo;

/**
 * Created by CaptainPete on 9/02/2016.
 */
public class MissingTextureHandler {

    // The solid fill texture, only loaded once.
    private static TextureInfo missingTexture = null;

    /**
     * If the the missing texture has not been generated, generate it. Then return the
     * missing texture.
     */
    public static TextureInfo getMissingTexture() {
        if (missingTexture == null) {
            generateMissingTexture();
        }
        return missingTexture;
    }


    private static void generateMissingTexture(){
        //TODO - Actually have a purple and white checkered pattern.
        missingTexture = SolidFillTextureHandler.getSolidFillTexture();
    }
}
