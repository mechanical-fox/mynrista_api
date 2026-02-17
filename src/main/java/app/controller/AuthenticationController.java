package app.controller;

import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import app.exception.UnauthorizedException;
import app.model.database.TokenEntity;
import app.model.database.UserEntity;
import app.model.in.AuthentificationBody;
import app.model.out.TokenResponse;
import app.repository.TokenRepository;
import app.repository.UserRepository;
import app.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = {"http://localhost:5173", "https://mynrista.fr","https://www.mynrista.fr"})
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "Authorization", scheme = "bearer")
@Tag(name = "Authentification")
@RestController
public class AuthenticationController {

    private static final long TOKEN_DURATION_SECOND = 3600;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

 
    @Operation(summary = "Demande d'un token d'authentification")
    @ApiResponse(responseCode = "200", description = "Succès")
    @ApiResponse(responseCode = "401", description = "Erreur d'authentification", content=@Content)
    @PostMapping(value="/auth", produces="application/json")
    public TokenResponse requestToken(@RequestBody AuthentificationBody body) throws UnauthorizedException, NoSuchAlgorithmException{

        if(body.getEmail() == null || body.getPassword() == null)
            throw new UnauthorizedException("The fields email, and password are mandatory");

        List<UserEntity> users = userRepository.queryByEmail(body.getEmail());

        if(users.size() == 0)
            throw new UnauthorizedException("User not Found");

        UserEntity user = users.get(0);
        String hashPassword = Util.hash(body.getPassword());

        if(user.getVerification_completed() == false)
            throw new UnauthorizedException("The Email of the user must be verified, before be able to connect");
        
        if(!hashPassword.equals(user.getHash_password()))
            throw new UnauthorizedException("Password incorrect");

        String token = Util.generateToken();
        OffsetDateTime expirationDate = OffsetDateTime.now().plusSeconds(AuthenticationController.TOKEN_DURATION_SECOND);
        TokenEntity tokenEntity = new TokenEntity(token, user, expirationDate);
        this.tokenRepository.save(tokenEntity);
        this.tokenRepository.deleteExpiredTokens();
        return new TokenResponse(token,AuthenticationController.TOKEN_DURATION_SECOND, user.getPseudo());
    }
}
