package gebd.games.boat.lidar.vis;

import blindmystics.input.CurrentInput;
import gebd.games.boat.lidar.LidarCalculationHandler;
import gebd.games.boat.lidar.LidarHelper;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.simple.SimpleQuad;
import renderables.r2D.text.String2D;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;
import java.awt.*;

/**
 * Created by CaptainPete on 2016-10-01.
 */
public class LidarVisualRepresentation extends UserInterface {

    private SimpleQuad lidarTexture;
    private String2D lidarDisplayText;
    private LidarCalculationHandler lidarCalculationHandler = null;
    private SimpleQuad boundaryBetweenLidarImages;

    public LidarVisualRepresentation(Vector2f relativePosition, Vector2f size) {
        super(relativePosition, size, 0);

        lidarTexture = addComponentToTop(new SimpleQuad(new Vector2f(), size, 0));
        lidarTexture.setTextureIsFlippedVertically(true);

        float yPosition = (size.y / 2.0f) - 50f;
        lidarDisplayText = addComponentToTop(new String2D("???", String2D.StringAlignment.MID_MIDDLE,
                new Vector2f(0, yPosition), new Vector2f(23, 23), "Courier", Font.PLAIN));
        lidarDisplayText.setBlend(new Vector4f(1, 0, 0, 1), 1);

//        boundaryBetweenLidarImages = addComponentToTop(new SimpleQuad(
//                new Vector2f(0, (size.y / 2f)),
//                new Vector2f(size.x, 5), 0));
//        boundaryBetweenLidarImages.setBlend(new Vector4f(1, 0, 0, 1), 1);

    }


    public void setLidarCalculationHandler(LidarCalculationHandler newLidarCalculationHandler) {
        if (this.lidarCalculationHandler == newLidarCalculationHandler) {
            //It's the same as before, don't change anything.
            return;
        }

        //A new lidarCalculationHandler has been set.
        this.lidarCalculationHandler = newLidarCalculationHandler;

        if (newLidarCalculationHandler == null) {

            //Hide everything.
            lidarTexture.setVisible(false);
            //boundaryBetweenLidarImages.setVisible(false);
            lidarDisplayText.setText("");

        } else {

            LidarHelper.DepthBufferDirection direction = newLidarCalculationHandler.getDepthBufferDirection();

            lidarTexture.setVisible(true);
            lidarTexture.setTexture(newLidarCalculationHandler.getFrameBuffer().getTexture());
            //boundaryBetweenLidarImages.setVisible(true);
            lidarDisplayText.setText(direction.name());

        }

    }

    @Override
    public void update(CurrentInput input, float delta) {
        super.update(input, delta);
    }
}
