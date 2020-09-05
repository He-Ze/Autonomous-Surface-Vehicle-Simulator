package byteStream;
 
//import java.nio.charset.Charset;
 
import java.nio.charset.Charset;
 
import byteStream.ByteStream;
import byteStream.Charsets;
 
public class SavingToAByteStream {
       
       
        int playerId_empty;
        String playerName_empty;
        double playerLife_empty;
       
       
        /*
         *  When creating it will ensure that it doesn't make a packet of over 1024 bytes.
         */
        int maximumByteArrayCapicity = 1024;
        ByteStream BS;
       
       
        Charset usedCharset = Charsets.UTF_8();
       
        public SavingToAByteStream(){
               
                log(playerId_empty);
                log(playerName_empty);
                log(playerLife_empty);
               
                BS = new ByteStream(maximumByteArrayCapicity);
                packByteArray(56, "Twisted Chicken!", 3.557);
                log(BS.getData());
               
                log(new String(BS.getData()), true);
               
                for(Byte b : BS.getData()){
                    if((int) b == 0){
                        log("_", false);
                    } else {
                        log((char) (int) b, false);
                    }
                }
                log("");
                for(Byte b : BS.getData()){
                    log(b, false);
                    log("[", false);
                    if((int) b == 0){
                        log("_", false);
                    } else {
                        log((char) (int) b, false);
                    }
                    
                    log("]", false);
                    log(", ", false);
                }
                log("-----");
                unpackByteArray(BS.getData());
               
                log(playerId_empty);
                log(playerName_empty);
                log(playerLife_empty);
        }
       
        public void packByteArray(int playerId, String playerName, double playerLife){
                BS.ByteBuffer().putInt(playerId);
                BS.putString(usedCharset, playerName);
                BS.ByteBuffer().putDouble(playerLife);
        }
       
        public void unpackByteArray(byte[] data){
                BS = new ByteStream(data);
               
                playerId_empty = BS.ByteBuffer().getInt();
                playerName_empty = BS.getString(usedCharset);
                playerLife_empty = BS.ByteBuffer().getDouble();
        }
       
       
       
       
        public static void main(String[] args) {
                new SavingToAByteStream();
        }
       
        private static void log(Object aMsg){
            System.out.println(String.valueOf(aMsg));
        }
        private static void log(Object aMsg, boolean printLine){
                if(printLine){
                        System.out.println(String.valueOf(aMsg));
                } else {
                        System.out.print(String.valueOf(aMsg));
                }
        }
}