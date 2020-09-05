package renderables.texture;

import loader.LoadedObjectHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import renderables.texture.TextureInfo;

import java.nio.ByteBuffer;

/**
 * Created by CaptainPete on 16/04/2016.
 */
public class CubemapTexture extends TextureInfo {

    private TextureInfo[] textureCubeMapData = new TextureInfo[6];

    public CubemapTexture(
            String texturePathPosX,
            String texturePathNexX,
            String texturePathPosY,
            String texturePathNegY,
            String texturePathPosZ,
            String texturePathNegZ){


        textureCubeMapData[0] = new TextureInfo(texturePathPosX);
        textureCubeMapData[1] = new TextureInfo(texturePathNexX);
        textureCubeMapData[2]  = new TextureInfo(texturePathPosY);
        textureCubeMapData[3]  = new TextureInfo(texturePathNegY);
        textureCubeMapData[4]  = new TextureInfo(texturePathPosZ);
        textureCubeMapData[5]  = new TextureInfo(texturePathNegZ);
    }

    @Override
    public LoadedObjectHandler.LoadStage[] stagesToPerform(){
        return new LoadedObjectHandler.LoadStage[] {
                LoadedObjectHandler.LoadStage.LOAD_DATA_FROM_FILE,
//				LoadStage.HANDLE_RAW_DATA,
//				LoadStage.LOAD_DEPENDENCIES,
        };
    }


    @Override
    public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
        for(int textureNo = 0; textureNo < textureCubeMapData.length; textureNo++) {
            textureCubeMapData[textureNo].loadRawDataFromFile(null);
        }
    }

    @Override
    public void handleRawData(LoadedObjectHandler<?> handler) {
        //Not called.
    }

    @Override
    public void loadDependencies(LoadedObjectHandler<?> handler) {
        //None.
    }

    @Override
    public void completeLoad(LoadedObjectHandler<?> handler) {

        int texId = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        TextureHandler.prepareTexture(GL13.GL_TEXTURE_CUBE_MAP, texId);

        loadTextureData(textureCubeMapData[0].get_tWidth(), textureCubeMapData[0].get_tHeight(), textureCubeMapData[0].getBuf(), GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X);
        loadTextureData(textureCubeMapData[1].get_tWidth(), textureCubeMapData[1].get_tHeight(), textureCubeMapData[1].getBuf(), GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X);
        loadTextureData(textureCubeMapData[2].get_tWidth(), textureCubeMapData[2].get_tHeight(), textureCubeMapData[2].getBuf(), GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y);
        loadTextureData(textureCubeMapData[3].get_tWidth(), textureCubeMapData[3].get_tHeight(), textureCubeMapData[3].getBuf(), GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y);
        loadTextureData(textureCubeMapData[4].get_tWidth(), textureCubeMapData[4].get_tHeight(), textureCubeMapData[4].getBuf(), GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z);
        loadTextureData(textureCubeMapData[5].get_tWidth(), textureCubeMapData[5].get_tHeight(), textureCubeMapData[5].getBuf(), GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z);

        /*
        for(int textureNo = 0; textureNo < textureCubeMapData.length; textureNo++) {
            //textureCubeMapData[textureNo]
            TextureInfo textureInfo = textureCubeMapData[textureNo];
            int texWidth = textureInfo.get_tWidth();
            int texHeight = textureInfo.get_tHeight();
            ByteBuffer buffer = textureInfo.getBuf();
            int textureType = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + textureNo;
            GL11.glTexImage2D(textureType, 0, GL11.GL_RGBA, texWidth, texHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        }
        */

//        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);



        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);



        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);


        setTextureId(texId);
    }

    private void loadTextureData(int width, int height, ByteBuffer buffer, int textureCubeMap){
        GL11.glTexImage2D(textureCubeMap, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
    }
}
