package app.controller;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import app.exception.UnauthorizedException;
import app.exception.BadRequestException;
import app.model.database.UserEntity;
import app.model.database.VisualNovelEntity;
import app.model.in.VisualNovelBody;
import app.repository.TokenRepository;
import app.repository.VisualNovelRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@CrossOrigin(origins = {"http://localhost:5173", "https://mynrista.fr","https://www.mynrista.fr"})
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "Authorization", scheme = "bearer")
@Tag(name = "Visual Novel")
@RestController
public class VisualNovelController {
    
    @Autowired
    private VisualNovelRepository visualNovelRepository;

    @Autowired
    private TokenRepository tokenRepository;


    @Operation(summary = "Création d'un Visual Novel")
    @SecurityRequirement(name = "Authorization")
    @ApiResponse(responseCode = "201", description = "Succès", content = @Content)
    @ApiResponse(responseCode = "400", description = "Requête invalide", content = @Content)
    @ApiResponse(responseCode = "401", description = "Authentification invalide", content = @Content)
    @PostMapping(value="/visual-novel", produces = "text/plain")
    public ResponseEntity<String> createVisualNovel(@RequestHeader HttpHeaders headers, @RequestBody VisualNovelBody body) 
    throws UnauthorizedException, BadRequestException{

        List<String> authorizations = headers.get("Authorization");

        if(authorizations == null || authorizations.size() == 0)
            throw new UnauthorizedException("");

        String[] parts = authorizations.get(0).trim().split(" ");

        if(parts.length != 2 || !"Bearer".equals(parts[0]))
            throw new UnauthorizedException("");

        List<UserEntity> users = tokenRepository.queryBearerByToken(parts[1]);

        if(users.size() != 1)
            throw new UnauthorizedException("");

        if(body.getTitle() == null || body.getImage_base64() == null || body.getDescription() == null){
            System.out.println("Title: " + body.getTitle());
            System.out.println("Image: " + body.getImage_base64());
            System.out.println("Description: " + body.getDescription());
            throw new BadRequestException("The following fields are mandatory: title, image_base64, description");
        }
            

        VisualNovelEntity visualNovel = new VisualNovelEntity(body.getTitle(), body.getImage_base64(), body.getDescription());
        visualNovelRepository.save(visualNovel);
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> answer = new ResponseEntity<String>("",responseHeaders,201);
        return answer;
    }

}
