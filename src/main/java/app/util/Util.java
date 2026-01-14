package app.util;


import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;




public class Util {

    /** Return a cryptographic hash, of the string given. The algorithm used is SHA-256. */
    public static String hash(String s) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
        String result = Util.bytesToHex(hash);
        return result;
    }


    /** Generate a random and unique token.*/
    public static String generateToken(){
        UUID uuid = UUID.randomUUID();
        String result = uuid.toString().replaceAll("-","");
        result = result.substring(0,16);
        return result;
    }


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


    /** A function to send a email, by using as sender a email you own in an SMTP server.
    * An email in an SMTP server, is different than an email from gmail or yahoo. 
     * @throws MessagingException */
    public static void sendMail(String SMTPServer, String emailSender, String passwordSender, 
    String emailReceiver, String title, String text, boolean debug) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTPServer);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "false"); 
                
        Session session=Session.getInstance(properties);
        session.setDebug(debug);
        Transport transport = session.getTransport("smtp");
        transport.connect(SMTPServer, emailSender, passwordSender);
             
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailSender));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailReceiver));
        message.setSubject(title);
        message.setText(text);
             
        transport.sendMessage(message, message.getAllRecipients());  
        
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
