import org.junit.Assert;
import org.junit.Test;

/**
 * Created by CaptainPete on 2016-10-12.
 */
public class SinTest {

    @Test
    public void TestCos() {

        for (int i = 0; i < 100; i++) {

            double angle = Math.random() * (Math.PI * 2.0);

            double negSinOfAngle = - Math.sin(angle);

            double sinOfNegAngle = Math.sin(-angle);

            double firstMinusOther = negSinOfAngle - sinOfNegAngle;

            Assert.assertTrue("OEAUTHGOUAEHT", Math.abs(firstMinusOther) < 0.0000001);

        }


    }
}
