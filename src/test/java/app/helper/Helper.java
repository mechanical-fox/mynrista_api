package app.helper;


import java.io.FileInputStream;
import java.io.IOException;


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

}
