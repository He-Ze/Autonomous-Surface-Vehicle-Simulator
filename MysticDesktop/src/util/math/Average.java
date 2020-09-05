package util.math;

/**
 * So I was thinking about calculating averages using
 * floating point numbers on the train, and I think there could
 * be a better way of storing the average than keeping a running
 * total. It's hard to prove however.
 * It would be interesting to test though!
 *
 * After testing this, it's VERY difficult to determine the whether
 * this method is more accurate or less accurate than the running total
 * method.
 *
 * It is however, more operations, hence, it should probably be abandoned.
 *
 * (@)See: AverageTest)
 *
 * Created by p3te on 26/10/16.
 */
public class Average {

    double currentAverage;
    double numElements;

    public void addNewValue(double newValue){

        numElements += 1.0;
        currentAverage *= ((numElements - 1.0) / (numElements));
        currentAverage += (newValue / numElements);

    }

    public double getCurrentAverage() {
        return currentAverage;
    }

    public double getNumElements() {
        return numElements;
    }

    public void clear(){
        currentAverage = 0.0;
        numElements = 0.0;
    }
}
