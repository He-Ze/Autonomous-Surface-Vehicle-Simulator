package gebd.util;

import java.util.*;

/**
 * Created by CaptainPete on 2016-10-15.
 */
public class TimerUtil {

    private static class TimeUtil {
        private long lastStartTime;
        private double runningTotal;
        private double numTimingEvents;

        public double getAverageTiming() {
            return runningTotal / numTimingEvents;
        }

        public void reset() {
            lastStartTime = -1;
            runningTotal = 0;
            numTimingEvents = 0;
        }
    }

    private static HashMap<String, TimeUtil> timings = new HashMap<>();

    public static double getTimingEventsOfKey(String key) {
        TimeUtil timerUtil = timings.get(key);
        if (timerUtil == null) {
            return 0;
        }
        return timings.get(key).numTimingEvents;
    }

    public static void addTimingStart(String key) {
        TimeUtil timerUtil = timings.get(key);
        if (timerUtil == null) {
            timerUtil = new TimeUtil();
            timings.put(key, timerUtil);
        }
        timerUtil.lastStartTime = System.nanoTime();
    }

    public static void addTimingEnd(String key) {
        TimeUtil timerUtil = timings.get(key);
        if (timerUtil == null) {
            System.err.println("TIMING ERROR! [1]");
            return;
        } else if (timerUtil.lastStartTime < 0) {
            if (timerUtil.lastStartTime < -2) {
                System.err.println("TIMING ERROR - " + key + " is never set!");
            } else {
                timerUtil.lastStartTime = -3;
            }
            return; //Ingore.
        }
        long timingDiff = System.nanoTime() - timerUtil.lastStartTime;
        timerUtil.runningTotal += timingDiff;
        timerUtil.numTimingEvents++;
    }

    public static void printAllCurrentTimingEvents() {
        for (Map.Entry<String, TimeUtil> entry : timings.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue().getAverageTiming() + "ns");
        }
    }

    public static void resetAllTimings() {
        for (Map.Entry<String, TimeUtil> entry : timings.entrySet()) {
            entry.getValue().reset();
        }
    }

    public static void printInExcelFormat() {
        ArrayList<String> keys = new ArrayList<>();
        keys.addAll(timings.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            System.out.println(key + "\t" + timings.get(key).getAverageTiming());
        }
    }
}
