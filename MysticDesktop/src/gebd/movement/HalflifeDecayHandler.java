package gebd.movement;

/**
 * Created by CaptainPete on 6/03/2016.
 */
public class HalflifeDecayHandler extends MovementHandler {

    protected float percentageEachSecond;

    /**
     * pretty much just a halflife decay.
     * @param percentageEachSecond - The percentage moved towards target each second
     * @require 0 >= percentageEachSecond <= 1 (Weird things will happen if it's not within these bounds)
     */
    public HalflifeDecayHandler(float percentageEachSecond){
        this.percentageEachSecond = percentageEachSecond;
    }

    @Override
    public void reset() {
        super.reset();
    }

    public void reset(float percentageEachSecond){
        reset();
        this.percentageEachSecond = percentageEachSecond;
    }

    @Override
    public void update(float delta){
        if(percentageEachSecond == 0){
            percentDistToGoal = 0f;
        }
        if(percentageEachSecond >= 1){
            percentDistToGoal = 1f;
        }
        float distMoved = (float) (1 - (1 / (Math.pow((1.0 / (1 - percentageEachSecond)), (delta / 1000)))));
        if(distMoved > 1){
            percentDistToGoal = 1;
        } else {
            float distanceRemaining = 1 - percentDistToGoal;
            percentDistToGoal += (distanceRemaining * distMoved);
        }
    }
}
