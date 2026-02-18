package app.controller;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import app.exception.UnauthorizedException;
import app.exception.BadRequestException;
import app.exception.NotFoundException;
import app.model.database.UserEntity;
import app.model.database.VisualNovelEntity;
import app.model.in.VisualNovelBody;
import app.model.out.VisualNovelResponse;
import app.repository.TokenRepository;
import app.repository.VisualNovelRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import app.util.Util;

@CrossOrigin(origins = {"http://localhost:5173", "https://mynrista.fr","https://www.mynrista.fr"})
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "Authorization", scheme = "bearer")
@Tag(name = "Visual Novel")
@RestController
public class VisualNovelController {
    
    @Autowired
    private VisualNovelRepository visualNovelRepository;

    @Autowired
    private TokenRepository tokenRepository;


    @Operation(summary = "Création d'un nouveau Visual Novel")
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

        if(body.getTitle() == null || body.getImage_base64() == null || body.getDescription() == null)
            throw new BadRequestException("The following fields are mandatory: title, image_base64, description"); 

        Date releaseDate = Util.toDate(body.getReleaseDate());

        VisualNovelEntity visualNovel = new VisualNovelEntity(body.getTitle(), body.getImage_base64(), body.getDescription(), releaseDate);
        visualNovelRepository.save(visualNovel);
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> answer = new ResponseEntity<String>("",responseHeaders,201);
        return answer;
    }

    @Operation(summary = "Liste exhaustive des Visual Novels existants")
    @ApiResponse(responseCode = "200", description = "Succès")
    @GetMapping(value="/visual-novel", produces="application/json")
    public List<VisualNovelResponse> getVisualNovel(){

        List<VisualNovelEntity> visualNovelList = visualNovelRepository.list();
        List<VisualNovelResponse> result = new ArrayList<VisualNovelResponse>();

        for(VisualNovelEntity entity : visualNovelList){
            VisualNovelResponse item = new VisualNovelResponse(entity);
            result.add(item);
        }
        return result;

    }

    @Operation(summary = "Recherche d'un Visual Novel par Id")
    @Parameter(name="id", example = "1",required = true)
    @ApiResponse(responseCode = "200", description = "Succès")
    @ApiResponse(responseCode = "404", description = "Ressource Inexistante", content = @Content)
    @GetMapping(value="/visual-novel/{id}", produces="application/json")
    public VisualNovelResponse getVisualNovelById(@PathVariable @NonNull Long id) throws NotFoundException{

        Optional<VisualNovelEntity> visualNovel= visualNovelRepository.findById(id);

        if(!visualNovel.isPresent())
            throw new NotFoundException("");

        return new VisualNovelResponse(visualNovel.get());

    }

    @Operation(summary = "Mise à jour d'un Visual Novel par Id")
    @SecurityRequirement(name = "Authorization")
    @Parameter(name="id", example = "1",required = true)
    @ApiResponse(responseCode = "204", description = "Succès", content = @Content)
    @ApiResponse(responseCode = "400", description = "Requête invalide", content = @Content)
    @ApiResponse(responseCode = "401", description = "Authentification invalide", content = @Content)
    @ApiResponse(responseCode = "404", description = "Ressource Inexistante", content = @Content)
    @PutMapping(value="/visual-novel/{id}", produces="text/plain")
    public ResponseEntity<String> patchVisualNovel(@RequestHeader HttpHeaders headers, @PathVariable @NonNull Long id, 
    @RequestBody VisualNovelBody body) throws NotFoundException, UnauthorizedException, BadRequestException{

        List<String> authorizations = headers.get("Authorization");

        if(authorizations == null || authorizations.size() == 0)
            throw new UnauthorizedException("");

        String[] parts = authorizations.get(0).trim().split(" ");

        if(parts.length != 2 || !"Bearer".equals(parts[0]))
            throw new UnauthorizedException("");

        List<UserEntity> users = tokenRepository.queryBearerByToken(parts[1]);

        if(users.size() != 1)
            throw new UnauthorizedException("");

        if(body.getTitle() == null || body.getImage_base64() == null || body.getDescription() == null)
            throw new BadRequestException("The following fields are mandatory: title, image_base64, description"); 

        Optional<VisualNovelEntity> optionalVisualNovel= visualNovelRepository.findById(id);

        if(!optionalVisualNovel.isPresent())
            throw new NotFoundException("");

        VisualNovelEntity visualNovel = optionalVisualNovel.get();
        visualNovel.setTitle(body.getTitle());
        visualNovel.setImage_base64(body.getImage_base64());
        visualNovel.setDescription(body.getDescription());
        String dateString = body.getReleaseDate();
        Date releaseDate = Util.toDate(dateString);
        visualNovel.setRelease_date(releaseDate);
        visualNovelRepository.save(visualNovel);

        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> result = new ResponseEntity<String>("", responseHeaders,204);

        return result;

    }

}
