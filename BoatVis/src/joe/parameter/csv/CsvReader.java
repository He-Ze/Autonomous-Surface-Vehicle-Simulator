package joe.parameter.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by CaptainPete on 8/5/2018.
 */
public class CsvReader {

    protected ArrayList<String> headers = null;
    protected ArrayList<ArrayList<String>> values = new ArrayList<>();
    private File csvFile;

    public CsvReader(File fileToRead){
        this.csvFile = fileToRead;

        BufferedReader br = null;
        String line = "";

        try {

            br = new BufferedReader(new FileReader(fileToRead));

            while (true){
                line = br.readLine();

                if (line == null) {
                    //We're done here.
                    break;
                }

                if (headers == null) {
                    headers = parseLine(line);
                } else {
                    values.add(parseLine(line));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ArrayList<String> parseLine(String line){

        ArrayList<String> result = new ArrayList<>();

        String[] lineSplit = line.split(",");

        for (String value : lineSplit) {
            result.add((value));
        }

        return result;
    }

    public int dataHeight(){
        return values.size();
    }

    public int dataWidth(){
        return values.get(0).size();
    }

    public File getCsvFile() {
        return csvFile;
    }

    public String getFilePathWithNameAppend(String appendedName){
        String directoryPath = getCsvFile().getParentFile().getPath();
        String fileName = getCsvFile().getName();
        String extension = "";
        String justTheName = "";
        for(int i = 0; i < fileName.length(); i++){
            int indexFromEnd = fileName.length() - (i + 1);
            char c = fileName.charAt(indexFromEnd);
            if(c == '.'){
                extension = fileName.substring(indexFromEnd + 1, fileName.length());
                justTheName = fileName.substring(0, indexFromEnd);
            }
        }

        return directoryPath + "/" + justTheName + appendedName + "." + extension;
    }
}

