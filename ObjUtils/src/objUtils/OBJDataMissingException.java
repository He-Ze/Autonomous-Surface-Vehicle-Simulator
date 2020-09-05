package objUtils;

/**
 * Created by alec on 2/12/15.
 */
public class OBJDataMissingException extends RuntimeException {
    public enum OBJData {
        POSITION("Missing position data!"),
        UV("Missing UV data!"),
        NORMAL("Missing Normal data!");

        String message;

        OBJData(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    public OBJDataMissingException(String message, int data) {
        super(message + " " + OBJData.values()[data]);
    }
}
