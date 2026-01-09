package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import app.util.PasswordManager;



@SpringBootApplication
public class Application {

	
    public static void main(String[] args) {

        PasswordManager.checkIfPasswordDeclared();
        SpringApplication.run(Application.class, args);
        
	}

}