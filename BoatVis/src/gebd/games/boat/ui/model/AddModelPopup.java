package gebd.games.boat.ui.model;

import blindmystics.input.CurrentInput;
import blindmystics.util.FileReader;
import gebd.ModelsetPlus;
import gebd.Render;
import gebd.games.boat.BoatModels;
import gebd.games.boat.ui.AddBuoyDislpay;
import loader.LoadedObjectHandler;
import renderables.framebuffer.imagegen.StaticModelTexture;
import renderables.r2D.Renderable2DUpdateHandler;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.simple.Button;
import renderables.r2D.simple.SimpleQuad;
import renderables.r2D.text.String2D;
import renderables.r3D.model.ModelsetModel;
import renderables.texture.generated.GeneratedTextureInfo;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by p3te on 8/11/16.
 */
public class AddModelPopup extends UserInterface {

    private ModelsetPlus modelset = BoatModels.getInstance();

    private SimpleQuad displayBlocker;
    private SimpleQuad backgroundQuad;

    private ArrayList<Button> modelButtons = new ArrayList<>();

    private AddBuoyDislpay parent;

    public AddModelPopup(AddBuoyDislpay parent) {
        super(new Vector2f(Render.getCentreOfScreen()), new Vector2f(500, 500), 0f);

        this.parent = parent;

        displayBlocker = addComponentToBottom(new SimpleQuad(new Vector2f(), Render.getScreenSize(), 0));
        displayBlocker.setBlend(1f, 1f, 1f, 0.3f, 1f);
        backgroundQuad = addComponentToTop(new SimpleQuad(new Vector2f(), size, 0));
        backgroundQuad.setBlend(new Vector4f(0.3f, 0.3f, 0.6f, 0.8f), 1f);

        //TODO - Fix textures
        modelButtons.add(createNewButton(BoatModels.ModelName.BUOY_STANDARD, new Vector2f(-100, 195)));
        modelButtons.add(createNewButton(BoatModels.ModelName.BUOY_TAPERED, new Vector2f(+100, 195)));
        modelButtons.add(createNewButton(BoatModels.ModelName.SHPERE, new Vector2f(-100, 65)));
        modelButtons.add(createNewButton(BoatModels.ModelName.CYLINDER, new Vector2f(+100, 65)));
        modelButtons.add(createNewButton(BoatModels.ModelName.LIGHT_BUOY, new Vector2f(-100, -65)));
        modelButtons.add(createNewButton(BoatModels.ModelName.DOUBLE_SIDED_PLANE, new Vector2f(+100, -65)));
        modelButtons.add(createNewButton(BoatModels.ModelName.CUBE_TEST, new Vector2f(-100, -195)));
        modelButtons.add(createNewButton(BoatModels.ModelName.UNIT_CYLINDER, new Vector2f(+100, -195)));

        Renderable2DUpdateHandler.addRenderableComponent(this);
    }


    @Override
    public void update(CurrentInput input, float delta) {
        super.update(input, delta);

        //Make sure the display is blocking all behind it.
        displayBlocker.setSize(Render.WIDTH, Render.HEIGHT);
        displayBlocker.setAbsolutePosition(Render.getCentreOfScreen());

    }

    private Button createNewButton(final BoatModels.ModelName modelName, Vector2f position){
        final Vector2f buttonSize = new Vector2f(100, 100 / Render.ASPECT_RATIO);

        Vector2f namePosition = new Vector2f(position.x, position.y - (buttonSize.y / 2f));
        String2D buttonName = addComponentToTop(new String2D(modelName.name(), String2D.StringAlignment.TOP_MIDDLE, namePosition, new Vector2f(12, 12), "Courier", Font.PLAIN));

        ModelsetModel model = modelset.getModel(modelName.name(), true);

        final String modelTexturePath = modelset.getTexturePath(modelName.name());

        //This is quite hacky!
        GeneratedTextureInfo generatedTextureInfo = new GeneratedTextureInfo(-1, -1, -1){
            @Override
            public void loadRawDataFromFile(LoadedObjectHandler<?> handler) {
                this.filePath = modelTexturePath;
                super.loadRawDataFromFile(handler);
                super.completeLoad(handler);
            }
        };
        generatedTextureInfo.loadRawDataFromFile(null);

        //TextureInfo modelTexture = TextureInfo.loadTexture(modelTexturePath);
        StaticModelTexture staticModelFbo = new StaticModelTexture((int) buttonSize.x, (int) buttonSize.y,
                model, generatedTextureInfo, Render.instance.getDefault3DShader(), modelset);

        //SimpleQuad textureBehindButton = addComponentToTop(new SimpleQuad(generatedTextureInfo, position, buttonSize, 0));
        SimpleQuad textureBehindButton = addComponentToTop(new SimpleQuad(staticModelFbo.getTexture(), position, buttonSize, 0));
        textureBehindButton.setTextureIsFlippedVertically(true);

        Button returnedButton = addComponentToTop(new Button(FileReader.asSharedFile("test_images/Buttonify.png"), position, buttonSize, 0){
            @Override
            protected void onRelease() {
                selectNewEntity(modelName);
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });
        return returnedButton;
    }

    public void selectNewEntity(BoatModels.ModelName modelName){
        parent.selectNewEntity(modelName);
    }
}
