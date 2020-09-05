package gebd.movement;

/**
 * Created by CaptainPete on 6/03/2016.
 */
public abstract class MovementHandler {

    protected float percentDistToGoal;

    public abstract void update(float delta);

    protected void reset() {
        this.percentDistToGoal = 0f;
    }

    public final float calculateCurrentValue(float startValue, float endValue) {
        return startValue + ((endValue - startValue) * percentDistToGoal);
    }

    public float getPercentDistToGoal() {
        return percentDistToGoal;
    }

}
