import org.junit.Assert;
import org.junit.Test;

/**
 * Created by p3te on 25/10/16.
 */
public class ModPercentTest {

    @Test
    public void TestModPositive() {

        for (int i = 0; i < 100; i++) {
            double val = Math.random() * 2.0;
            double other = val;
            if (other >= 1.0) {
                other -= 1.0;
            }
            double other2 = val % 1.0;

            Assert.assertTrue("OEAUTHGOUAEHT", Math.abs(other - other2) < 0.0000001);
        }

    }

    @Test
    public void TestModNegative() {

        for (int i = 0; i < 100; i++) {
            double val = Math.random() * -2.0;
            double other = val;
            if (other <= -1.0) {
                other += 1.0;
            }
            double other2 = val % 1.0;

            Assert.assertTrue("OEAUTHGOUAEHT", Math.abs(other - other2) < 0.0000001);
        }

    }
}
