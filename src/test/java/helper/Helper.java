package helper;


import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Helper {


    /** This function read the content of a file, and return it. This function read all the file without limit
     * of size, and so will have problem to proceed large files (500Mo or more).*/
    public static String readAll(String filename) throws IOException{

        try(FileInputStream file_in = new FileInputStream(filename)){
            int length = file_in.available();
            byte[] buffer = new byte[length];
            int readed = file_in.read(buffer);

            if(readed != length)
                throw new IOException("Error I/O during reading the file " + filename);

            return new String(buffer);
        }
        
    }

    /** Return a cryptographic hash, of the string given. The algorithm used is SHA-256. */
    public static String hash(String s) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
        String result = Helper.bytesToHex(hash);
        return result;
    }

    /** Convert an array of bytes, to a hexadecimal string */
    private static String bytesToHex(byte[] hash) {

        StringBuilder hexString = new StringBuilder(2 * hash.length);

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);

            if(hex.length() == 1) 
                hexString.append('0');
        
            hexString.append(hex);
        }

        return hexString.toString();
    }

}
