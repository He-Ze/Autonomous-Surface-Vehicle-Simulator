package gebd.movement;

import javax.vecmath.Vector2f;

/**
 * Created by CaptainPete on 4/03/2016.
 */
public class SmoothMovementHandler extends MovementHandler {

    float percentageTimeTravelled;
    float timeElapsed;
    float velocity;
    float maxVelocity;
    float duration;
    boolean complete;

    /**
     * S = ut + (1/2) * a * (t ^ 2)
     * Simplified:
     * S = avgVel * t
     *
     * @param duration
     */
    public SmoothMovementHandler(float duration){
        reset(duration);
    }

    @Override
    public void reset(){
        reset(duration);
    }

    public void reset(float duration){
        super.reset();
        this.duration = duration;
        float averageVelocity = 1 / duration;
        maxVelocity = averageVelocity * 2f;
        percentageTimeTravelled = 0f;
        timeElapsed = 0;
        velocity = 0f;
        complete = false;
    }

    @Override
    public void update(float delta){
        if(complete){
            return;
        }
        timeElapsed += (delta);

        percentageTimeTravelled = timeElapsed / duration;

        if(percentageTimeTravelled >= 1f){
            velocity = 0f;
            complete = true;
            percentDistToGoal = 1f;
        } else {
            if(percentageTimeTravelled < 0.5f){
                velocity = maxVelocity * percentageTimeTravelled * 2f;
            } else {
                velocity = (maxVelocity * 2f) - (maxVelocity * percentageTimeTravelled * 2f);
            }
        }

        percentDistToGoal += velocity * (delta);
        if(percentDistToGoal >= 1f){
            percentDistToGoal = 1f;
        }
    }

}
