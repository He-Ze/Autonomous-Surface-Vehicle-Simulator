package old.util;

import java.nio.ByteBuffer;

/**
 * Created by CaptainPete on 7/03/2016.
 */
public class ByteBufferInfo {

    public static String printByteBufferInfo(ByteBuffer buffer) {

        int currentPosition = buffer.position();

        StringBuilder result = new StringBuilder();

        result.append("________" + buffer.toString() + "________");
        result.append("\n");
        result.append("buffer.position() = " + buffer.position());
        result.append("\n");
        result.append("buffer.capacity() = " + buffer.capacity());
        result.append("\n");
        result.append("buffer.limit() = " + buffer.limit());
        result.append("\n");

        buffer.position(0);
        result.append(new String(buffer.array()));
        result.append("\n");

        for (byte b : buffer.array()) {
            result.append((int) (b & 0xFF));
            result.append(", ");
        }
        result.append("\n");

        result.append(bytesToHex(buffer.array()));
        result.append("\n");

        result.append("________________________");
        result.append("\n");

        buffer.position(currentPosition);

        return result.toString();
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
