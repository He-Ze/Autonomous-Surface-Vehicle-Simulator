package gebd.shaders.shaders.earthy;

import composites.entities.Entity;
import gebd.shaders.shaders.Textured3DShader;

/**
 * Created by p3te on 19/05/16.
 */
public class EarthInsolShader extends Textured3DShader {

    protected int worldDayTextureLocation;
    protected int worldNightTextureLocation;
    protected int worldWaterTextureLocation;
    protected int dayOfYearLocation;

    public EarthInsolShader(int vertexShaderId, int fragmentShaderId) {
        super(vertexShaderId, fragmentShaderId);
    }

    @Override
    protected void setupUniformVariables() {
        super.setupUniformVariables();

        worldDayTextureLocation = getUniformLocation("worldDayTexture");
        worldNightTextureLocation = getUniformLocation("worldNightTexture");
        worldWaterTextureLocation = getUniformLocation("worldWaterTexture");

        dayOfYearLocation = getUniformLocation("dayOfYear");

    }

    public void connectTextureUnits(){
        loadInt(worldDayTextureLocation, 0);
        loadInt(worldNightTextureLocation, 1);
        loadInt(worldWaterTextureLocation, 2);
    }

    @Override
    public void prepare() {
        super.prepare();
        connectTextureUnits();
    }

    public void setDayOfYear(int dayOfYear) {
        loadInt(dayOfYearLocation, dayOfYear);
    }

    @Override
    public void prepareEntity(Entity e) {
        setModelPosition(e.getPosition());
        setModelSize(e.getSize());
        setModelRotation(e.getRotation());
    }
}
