package blindmystics.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by alec on 17/04/16.
 */
public class FileReader {
    public static InputStream asInputStream(String identifier) {
        InputStream stream = null;
        File file = getFile(identifier);
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("Could not read file - " + identifier);
        }
        return stream;
    }

    public static File getFile(String path) {
        return new File(path);
    }

    public static String RESOURCES_FILES_DIRECTORY_PREFIX = "";

    public static String COMMON_FILES_DIRECTORY = "../MysticDesktop/res/";

    public static String ROOT_FOLDER = "";

    public static String asSharedFile(String filePath) {
        return ROOT_FOLDER + COMMON_FILES_DIRECTORY + filePath;
    }

    public static String asResource(String filePath) {
        return ROOT_FOLDER + RESOURCES_FILES_DIRECTORY_PREFIX + filePath;
    }

}
