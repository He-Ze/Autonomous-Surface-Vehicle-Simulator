import org.junit.Assert;
import org.junit.Test;

/**
 * Created by CaptainPete on 2016-10-12.
 */
public class CosTest {

    @Test
    public void TestCos() {

        for (int i = 0; i < 100; i++) {

            double angle = Math.random() * (Math.PI * 2.0);

            double cosOfAngle = Math.cos(angle);

            double the180MinusAngle = Math.PI - angle;

            double cosOfOtherAngle = Math.cos(the180MinusAngle);

            double cos1PlusCosOther = cosOfAngle + cosOfOtherAngle;

            Assert.assertTrue("OEAUTHGOUAEHT", Math.abs(cos1PlusCosOther) < 0.0000001);

        }


    }
}
