import org.junit.Test;
import util.list.ExposedArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by CaptainPete on 2016-11-09.
 */
public class ArrayTestThing {


    @Test
    public void testTheExposedArray() {


        ExposedArrayList<String> lolTest = new ExposedArrayList<>();
        lolTest.add("A");
        lolTest.add("C");
        lolTest.add("B");

        for (String string : lolTest) {
            System.out.println(string);
        }

        Arrays.sort(lolTest.exposedArray(), 0, lolTest.size());

        System.out.println(" -- Sorted -- ");

        for (String string : lolTest) {
            System.out.println(string);
        }

    }

    private class StringCompritorTest implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {

            int lengthToCompare = Math.min(o1.length(), o2.length());

            for (int i = 0; i < lengthToCompare; i++) {
                char o1Char = o1.charAt(i);
                char o2Char = o1.charAt(i);

                if (o1Char < o2Char) {
                    return 1;
                } else if (o1Char > o2Char) {
                    return -1;
                }

            }

            return 0;
        }
    }

    private String[] setupArray() {
        int arraySize = 10000;
        int stringSize = 50;

        String[] result = new String[arraySize];
        for (int i = 0; i < arraySize; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < stringSize; j++) {

                int charNum = (i + j) % 255;
                char c = (char) (charNum);
                sb.append(c);

            }
            result[i] = sb.toString();
        }

        return result;
    }

    @Test
    public void timeTestExposed(){

        ExposedArrayList<String> exposedStringList = new ExposedArrayList<>();
        String[] array = setupArray();
        for (String string : array) {
            exposedStringList.add(string);
        }

        for (int i = 0; i < 20000; i++) {
            Collections.shuffle(exposedStringList);
            Arrays.sort(exposedStringList.exposedArray(), 0, exposedStringList.size());
        }

        //48s 719ms
        //48MB

    }

    @Test
    public void timeTestArrayList(){

        ArrayList<String> exposedStringList = new ArrayList<>();
        String[] array = setupArray();
        for (String string : array) {
            exposedStringList.add(string);
        }

        for (int i = 0; i < 20000; i++) {
            Collections.shuffle(exposedStringList);
            Collections.sort(exposedStringList);
        }

        //49s 970ms
        //39Mb

    }


}
