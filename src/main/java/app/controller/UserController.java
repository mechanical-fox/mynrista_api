
package app.controller;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
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

import app.exception.BadRequestException;
import app.model.database.UserEntity;
import app.model.in.UserBody;
import app.model.out.ValidityResponse;
import app.repository.UserRepository;
import app.util.Util;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@CrossOrigin(origins = {"http://localhost:4200", "https://mynrista.fr","https://www.mynrista.fr"})
@Tag(name = "User")
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @Operation(summary = "Vérifie si la création d'un utilisateur est possible")
    @ApiResponse(responseCode = "200", description = "Succès")
    @Parameter(name="pseudo", in =ParameterIn.PATH, description = "pseudo utilisateur", example = "Itsuki")
    @Parameter(name="email", in =ParameterIn.PATH, description = "email utilisateur", example = "itsuki@gmail.com")
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

        othersRules.add("Les mots de passe doivent avoir une longueur >= 6");
        othersRules.add("Les champs pseudo, email, et password sont obligatoires");
            
        return new ValidityResponse(isRegistrationAccepted, pseudoAlreadyExisting, emailAlreadyExisting, othersRules);
    }

    @Operation(summary = "Création d'un utilisateur")
    @ApiResponse(responseCode = "201", description = "Succès")
    @ApiResponse(responseCode = "400", description = "Requête invalide", content = @Content)
    @PostMapping(value="/users", produces = "application/json")
    public ResponseEntity<String> createUser(@RequestBody UserBody body) throws IOException, BadRequestException, NoSuchAlgorithmException{

        List<UserEntity> users1 = userRepository.queryByUser(body.getPseudo());
        List<UserEntity> users2 = userRepository.queryByEmail(body.getEmail());

        if(body.getEmail() == null || body.getPseudo() == null || body.getPassword() == null){
            String message = "Requête invalide. \n";
            message += "Merci de renseigner en body les champs suivants: email, pseudo, password";
            throw new BadRequestException(message);
        }

        if(users1.size() > 0)
            throw new BadRequestException("Utilisateur déjà existant");
        if(users2.size() > 0)
            throw new BadRequestException("Email déjà existant");
        if(body.getPassword().length() < 6)
            throw new BadRequestException("Les mots de passe doivent être de longueur 6 ou plus");

        String hash = Util.hash(body.getPassword());
        UserEntity user = new UserEntity(body.getPseudo(), body.getEmail(), hash, false);
        userRepository.save(user);
        
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> entity = new ResponseEntity<>("", headers, 201);
        return entity;
    }


}
