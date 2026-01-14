package app.util;


public class PasswordManager {

    static String mockPassword = null;

    /** A function used during unit test, to ask to don't retrieve password from the environment variable 
     * "MYNRISTA_EMAIL_PASSWORD" than will not necessary be declared. But to retrieve it from the value passed in 
     * argument. The goal is of don't crash during unit test, with a message "MYNRISTA_EMAIL_PASSWORD not declared".*/
    public static void mockPassword(String password){
        PasswordManager.mockPassword = password;
    }

    /** A function than check if the password is declared in the environment variable "MYNRISTA_EMAIL_PASSWORD"
     * and else will display an error message, and exit. */
    public static void checkIfPasswordDeclared(){

        if(System.getenv("MYNRISTA_EMAIL_PASSWORD") == null && mockPassword == null){

            String msg = "\n\nTo function the application need the following variable :\n\n";
            msg += "    MYNRISTA_EMAIL_PASSWORD\n";
            msg += "        The password used to connect to the e-mail of mynrista, to send e-mails to users\n\n";

            System.err.println(msg);
            System.exit(-1);
        }   
    }


    /** Return the password necessary to connect to the e-mail of mynrista*/
    public static String getMynristaEmailPassword(){
        PasswordManager.checkIfPasswordDeclared();

        if(mockPassword != null)
            return mockPassword;
        else
            return System.getenv("MYNRISTA_EMAIL_PASSWORD");
    }
    
}
