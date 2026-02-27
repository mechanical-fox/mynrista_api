
package app.controller;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import app.example.ExampleDocHtml;
import app.exception.BadRequestException;
import app.model.database.UserEntity;
import app.model.in.UserBody;
import app.model.out.ValidityResponse;
import app.repository.UserRepository;
import app.util.PasswordManager;
import app.util.Util;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;


@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:4173", "https://mynrista.fr","https://www.mynrista.fr"})
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "Authorization", scheme = "bearer")
@Tag(name = "Inscription")
@RestController
public class InscriptionController {

    @Autowired
    private UserRepository userRepository;

    @Value("${email.sender}")
    private String emailSender;
    @Value("${email.SMTP-server}")
    private String SMTPServer;
    @Value("${email.template.server-link}")
    private String server_link;
    @Value("${email.enabled}")
    private Boolean emailEnabled;

    private static final String RESOURCES_FOLDER = "src/main/resources";


    @Operation(summary = "Vérifie si la création d'un utilisateur est possible")
    @ApiResponse(responseCode = "200", description = "Succès")
    @Parameter(name="pseudo", in =ParameterIn.PATH, example = "Itsuki")
    @Parameter(name="email", in =ParameterIn.PATH, example = "itsuki@gmail.com")
    @GetMapping(value="/users/validity/{pseudo}/{email}", produces = "application/json")
    public ValidityResponse checkValidityRegistration(@PathVariable String pseudo, @PathVariable String email) {

        List<UserEntity> users1 = userRepository.queryByUser(pseudo);
        List<UserEntity> users2 = userRepository.queryByEmail(email);
        Boolean isRegistrationAccepted = true;
        Boolean pseudoAlreadyExisting = false;
        Boolean emailAlreadyExisting = false;
        List<String> othersRules = new ArrayList<String>();

        if(users1.size() > 0)
            pseudoAlreadyExisting = true;
        if(users2.size() > 0)
            emailAlreadyExisting = true;
        if(pseudoAlreadyExisting || emailAlreadyExisting)
            isRegistrationAccepted = false;

        othersRules.add("The passwords must be of length >= 6");
        othersRules.add("The fields pseudo, email, and password are mandatories");
            
        return new ValidityResponse(isRegistrationAccepted, pseudoAlreadyExisting, emailAlreadyExisting, othersRules);
    }


    
    @Operation(summary = "Création d'un nouvel utilisateur")
    @ApiResponse(responseCode = "201", description = "Succès", content = @Content)
    @ApiResponse(responseCode = "400", description = "Requête invalide", content = @Content)
    @PostMapping(value="/users", produces = "text/plain")
    public ResponseEntity<String> createUser(@RequestBody UserBody body) throws  BadRequestException, 
    NoSuchAlgorithmException, MessagingException, IOException{

        List<UserEntity> users1 = userRepository.queryByUser(body.getPseudo());
        List<UserEntity> users2 = userRepository.queryByEmail(body.getEmail());

        if(body.getEmail() == null || body.getPseudo() == null || body.getPassword() == null){
            String message = "Invalid Request. \n";
            message += "The following fields are mandatory: email, pseudo, password";
            throw new BadRequestException(message);
        }

        if(users1.size() > 0)
            throw new BadRequestException("User already existing");
        if(users2.size() > 0)
            throw new BadRequestException("Email already existing");
        if(body.getPassword().length() < 6)
            throw new BadRequestException("The passwords must be of length >= 6");

        String hash = Util.hash(body.getPassword());
        String token = Util.generateToken();
        String verification_link = this.server_link + "/check-mail/" + token; 
        UserEntity user = new UserEntity(body.getPseudo(), body.getEmail(), hash, verification_link, false);
        
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("VERIFICATION_LINK", verification_link);
        ResponseEntity<String> entity = new ResponseEntity<>("", headers, 201);
        String mailTitle = "Confirmation inscription mynrista";
        String mailContent = Util.readAll(InscriptionController.RESOURCES_FOLDER + "/mail_template.txt");
        mailContent = mailContent.replaceAll("<pseudo>", user.getPseudo());
        mailContent = mailContent.replaceAll("<server-link>", this.server_link);
        mailContent = mailContent.replaceAll("<token>", token);
        
        try{
            if(emailEnabled){
                String emailPassword = PasswordManager.getMynristaEmailPassword();
                Util.sendMail(this.SMTPServer,this.emailSender,emailPassword,body.getEmail(), mailTitle, mailContent, false);
            }
                
        }
        catch(SendFailedException err){
            // Even if the receiver email is incorrect, we prevent the server from a crash
            // In database, the user will not be validated (Verification link no clicked)
            // The catch doesn't prevent the exception, if the adress for the SMTP Server is incorrect. And it shouldn't.
        }
        
        userRepository.save(user); // The user is saved only if the Email coud be send (SMTP Server correct)
        return entity;
    }



    @Operation(summary = "Lien de confirmation d'inscription (Envoyé par email)")
    @ApiResponse(responseCode = "200", description = "Succès", content=@Content(examples={@ExampleObject(value=ExampleDocHtml.htmlExample)}))
    @Parameter(name="token", in =ParameterIn.PATH, example = "24de8968a01e4e39")
    @GetMapping(value="/check-mail/{token}", produces = "text/html")
    public String confirmInscription(@PathVariable String token) throws IOException {

        String verification_link = this.server_link + "/check-mail/" + token; 
        List<UserEntity> users = userRepository.queryByVerificationLink(verification_link);
        String htmlResponse = "";

        if(users.size() == 0){
            htmlResponse = Util.readAll(InscriptionController.RESOURCES_FOLDER + "/verification_failure.html");
            htmlResponse = htmlResponse.replaceAll("<!-- token -->",  token );
        }
        else{
            htmlResponse = Util.readAll(InscriptionController.RESOURCES_FOLDER + "/verification_success.html");
            htmlResponse = htmlResponse.replaceAll("<!-- email -->", users.get(0).getEmail());

            for(UserEntity user : users){
                user.setVerification_completed(true);
                userRepository.save(user);
            }
        }

        return htmlResponse;
    }


}
