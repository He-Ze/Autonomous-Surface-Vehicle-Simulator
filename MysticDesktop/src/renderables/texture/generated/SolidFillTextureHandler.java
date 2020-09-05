package renderables.texture.generated;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import renderables.texture.TextureGenerator;
import renderables.texture.TextureHandler;
import renderables.texture.TextureInfo;

/**
 * A class that generates a simple texture that is only 1 colour. This texture can be used for
 * uniform rectangles, or it can be used when the texture failed to load.
 *
 * @author P3TE
 */
public class SolidFillTextureHandler {

  // The solid fill texture, only loaded once.
  private static TextureInfo solidFillTexture = null;

  /**
   * If the the solid fill texture has not been generated, generate it. Then return the solid fill
   * texture.
   */
  public static TextureInfo getSolidFillTexture() {
    if (solidFillTexture == null) {
      generateSolidFillTexture();
    }
    return solidFillTexture;
  }

  /**
   * Generates a solid fill texture by creating a buffered image of white, then turnming that into
   * an OpenGl texture
   */
  private static void generateSolidFillTexture() {
    final int BYTES_PER_PIXEL = 4; // RGBA
    // Image of size 1X1
    final int textureWidth = 1;
    final int textureHeight = 1;
    ByteBuffer texture =
            BufferUtils.createByteBuffer(textureWidth * textureHeight * BYTES_PER_PIXEL); // 4 for RGBA.

    // Set the texture to be pure white.
    for(int y = 0; y < textureHeight; y++){
      for(int x = 0; x < textureHeight; x++){
        texture.put((byte) 0xFF);
        texture.put((byte) 0xFF);
        texture.put((byte) 0xFF);
        texture.put((byte) 0xFF);
      }
    }
    texture.flip();

    // Generate the texture.
    solidFillTexture = TextureInfo.generateNewTexture("[GENERATED] - Solid Fill Texture", TextureHandler.DEFAULT_TEXTURE_UNIT,
            textureWidth, textureHeight, texture, GL11.GL_NEAREST, GL11.GL_NEAREST);
  }

}
