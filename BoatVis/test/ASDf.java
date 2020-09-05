import org.junit.Test;

/**
 * Created by CaptainPete on 2016-12-03.
 */
public class ASDf {

    @Test
    public void doStuff() {

        String item1 = "Hatreds Heart";
        String item2 = "Hatreds Soul";

        String together = item1 + item2;

        together = together.toLowerCase();

        int total = 0;

        for (int i = 0; i < together.length(); i++) {
            char c = together.charAt(i);
            if (c >= 'a' && c <= 'z') {
                int valToAdd = ((int) c) - ((int) 'a');
                total += valToAdd;
                System.out.println("valToAdd (" + c + ") = " + valToAdd);
            }
        }

        System.out.println("total = " + total);

    }

}
