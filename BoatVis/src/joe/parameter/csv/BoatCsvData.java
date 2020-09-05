package joe.parameter.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by CaptainPete on 8/5/2018.
 */
public class BoatCsvData extends CsvReader {

    public static final int TIME_COLUMN = 0;
    public static final int SPEED_COLUMN = 1;
    public static final int HEADING_COLUMN = 2;
    public static final int HEADING_RATE_COLUMN = 3;
    public static final int LEFT_MOTOR_PERCENTAGE_COLUMN = 4;
    public static final int RIGHT_MOTOR_PERCENTAGE_COLUMN = 5;

    private float[][] data;

    private float[][] simulatorData;

    public BoatCsvData(File fileToRead) {
        super(fileToRead);
        parseData();
    }

    private void parseData(){
        data = new float[dataWidth()][dataHeight()];
        simulatorData = new float[dataWidth()][dataHeight()];

        for(int y = 0; y < dataHeight(); y++) {
            ArrayList<String> row = values.get(y);
            for(int x = 0; x < dataWidth(); x++) {
                String value = row.get(x);
                float convertedValue = Float.valueOf(value);
                data[x][y] = convertedValue;
            }
        }

    }

    //Write out

    public void writeToFile(String filePath) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));

        for(int i = 0; i < headers.size(); i++){
            if(i != 0){
                writer.append(",");
            }
            String header = headers.get(i);
            writer.append(header);
        }
        writer.append(",");
        for(int i = 0; i < headers.size(); i++){
            if(i != 0){
                writer.append(",");
            }
            String header = headers.get(i);
            writer.append("sim_");
            writer.append(header);
        }
        writer.append("\n");

        for(int y = 0 ; y < dataHeight(); y++){
            for (int x = 0; x < dataWidth(); x++) {
                writer.append("" + data[x][y]);
                writer.append(",");
            }
            for (int x = 0; x < dataWidth(); x++) {
                writer.append("" + simulatorData[x][y]);
                if(x == (dataWidth() - 1)){
                    writer.append("\n");
                } else {
                    writer.append(",");
                }
            }
        }

        writer.close();

    }

    //Csv:

    public float[] getTimes(){
        return data[TIME_COLUMN];
    }

    public float[] getSpeeds(){
        return data[SPEED_COLUMN];
    }

    public float[] getHeadings(){
        return data[HEADING_COLUMN];
    }

    public float[] getHeadingRates(){
        return data[HEADING_RATE_COLUMN];
    }

    public float[] getLeftMotorPercentages(){
        return data[LEFT_MOTOR_PERCENTAGE_COLUMN];
    }

    public float[] getRightMotorPercentages(){
        return data[RIGHT_MOTOR_PERCENTAGE_COLUMN];
    }

    //Simulator:

    public float[] getSimTimes(){
        return simulatorData[TIME_COLUMN];
    }

    public float[] getSimSpeeds(){
        return simulatorData[SPEED_COLUMN];
    }

    public float[] getSimHeadings(){
        return simulatorData[HEADING_COLUMN];
    }

    public float[] getSimHeadingRates(){
        return simulatorData[HEADING_RATE_COLUMN];
    }

    public float[] getSimLeftMotorPercentages(){
        return simulatorData[LEFT_MOTOR_PERCENTAGE_COLUMN];
    }

    public float[] getSimRightMotorPercentages(){
        return simulatorData[RIGHT_MOTOR_PERCENTAGE_COLUMN];
    }

    //Quality Metrics:

    public static double getMean(float[] values){
        double total = 0f;
        for(int i = 0; i < values.length; i++) {
            total += values[i];
        }
        total /= ((double) values.length);
        return total;
    }

    public static double getSsTot(float[] values){
        double mean = getMean(values);

        double sstot = 0f;
        for(int i = 0; i < values.length; i++) {
            double valNotSquared = (values[i] - mean);
            double valSquared = valNotSquared * valNotSquared;
            sstot += valSquared;
        }

        return sstot;
    }

    public static double getSsRes(float[] values, float[] fnValues){

        double ssRes = 0f;
        for(int i = 0; i < values.length; i++) {
            double valNotSquared = (values[i] - fnValues[i]);
            double valSquared = valNotSquared * valNotSquared;
            ssRes += valSquared;
        }

        return ssRes;
    }

    public static double getRSquared(float[] values, float[] fnValues){
        double sstot = getSsTot(values);
        double ssres = getSsRes(values, fnValues);
        double r2 = 1.0f - (ssres / sstot);
        return r2;
    }
}
