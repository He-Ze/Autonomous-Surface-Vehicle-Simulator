import org.junit.Assert;
import org.junit.Test;
import util.math.Average;

import java.math.BigDecimal;

/**
 * Created by p3te on 25/10/16.
 */
public class AverageTest {

    @Test
    public void TestNewAverageFunction() {

        int numVals = 100000;
        double range = 100000;
        double runningTotal = 0;
        Average average = new Average();
        BigDecimal accurateRunningTotal = new BigDecimal(0.0);

        for (int i = 0; i < numVals; i++) {
            double val = (Math.random() * range) - (range / 2.0);

            runningTotal += val;
            average.addNewValue(val);

            accurateRunningTotal = accurateRunningTotal.add(new BigDecimal(val));

        }

        double averageWithRunningTotal = runningTotal / numVals;
        double averageWithRunningAverage = average.getCurrentAverage();
        Assert.assertTrue("OEAUTHGOUAEHT", Math.abs(averageWithRunningTotal - averageWithRunningAverage) < 0.0000001);


        BigDecimal accurateAverage = new BigDecimal(0.0);
        accurateAverage = accurateAverage.add(accurateRunningTotal);
        accurateAverage = accurateAverage.divide(new BigDecimal(numVals));

        System.out.println("accurateAverage = " + accurateAverage);

        BigDecimal distToAccurateAverageRunningTotal = new BigDecimal(0.0);
        distToAccurateAverageRunningTotal = distToAccurateAverageRunningTotal.add(accurateAverage);
        distToAccurateAverageRunningTotal = distToAccurateAverageRunningTotal.subtract(new BigDecimal(averageWithRunningTotal));
        distToAccurateAverageRunningTotal = distToAccurateAverageRunningTotal.abs();
        System.out.println("distToAccurateAverageRunningTotal = " + distToAccurateAverageRunningTotal);

        BigDecimal distToAccurateAverageClass = new BigDecimal(0.0);
        distToAccurateAverageClass = distToAccurateAverageClass.add(accurateAverage);
        distToAccurateAverageClass = distToAccurateAverageClass.subtract(new BigDecimal(averageWithRunningAverage));
        distToAccurateAverageClass = distToAccurateAverageClass.abs();
        System.out.println("distToAccurateAverageClass = " + distToAccurateAverageClass);

        if (distToAccurateAverageClass.compareTo(distToAccurateAverageRunningTotal) == 0) {
            System.out.println("They have the same accuracy.");
        } else if (distToAccurateAverageClass.compareTo(distToAccurateAverageRunningTotal) > 0) {
            System.out.println("Running total was more accurate.");
        } else {
            System.out.println("Running average was more accurate.");
        }
    }

}
