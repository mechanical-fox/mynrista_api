package app.controller;


import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import app.model.database.TagEntity;
import app.model.database.UserEntity;
import app.model.database.VisualNovelEntity;
import app.model.in.VisualNovelBody;
import app.model.out.VisualNovelResponse;
import app.repository.TagRepository;
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

    @Autowired
    private TagRepository tagRepository;


    @Operation(summary = "Création d'un nouveau Visual Novel")
    @SecurityRequirement(name = "Authorization")
    @ApiResponse(responseCode = "201", description = "Succès", content = @Content)
    @ApiResponse(responseCode = "400", description = "Requête invalide", content = @Content)
    @ApiResponse(responseCode = "401", description = "Authentification invalide", content = @Content)
    @PostMapping(value="/visual-novel", produces = "text/plain")
    public ResponseEntity<String> createVisualNovel(@RequestHeader HttpHeaders headers, @RequestBody VisualNovelBody body) 
    throws UnauthorizedException, BadRequestException{

        if(!this.isAuthorized(headers))
            throw new UnauthorizedException("");

        if(body.getTitle() == null || body.getImage_base64() == null || body.getDescription() == null)
            throw new BadRequestException("The following fields are mandatory: title, image_base64, description"); 

        Map<String, TagEntity> tagMap = this.mapTags(body.getTags());
        VisualNovelEntity visualNovel = new VisualNovelEntity(body, tagMap);
        visualNovelRepository.save(visualNovel);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("LINK", "/visual-novel/" + visualNovel.getId());
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

    @Operation(summary = "Liste de 4 Visual Novels du Top \"Nouveautés et Tendances\" ")
    @ApiResponse(responseCode = "200", description = "Succès")
    @GetMapping(value="/visual-novel/top-new", produces="application/json")
    public List<VisualNovelResponse> getTopNewVisualNovels(){

        List<VisualNovelEntity> visualNovelList = visualNovelRepository.topNewVisualNovels();
        List<VisualNovelResponse> result = new ArrayList<VisualNovelResponse>();

        for(VisualNovelEntity entity : visualNovelList){
            VisualNovelResponse item = new VisualNovelResponse(entity);
            result.add(item);
        }

        return result;

    }

    @Operation(summary = "Liste de 6 Visual Novels du Top \"Meilleurs Evaluations\" ")
    @ApiResponse(responseCode = "200", description = "Succès")
    @GetMapping(value="/visual-novel/top-rating", produces="application/json")
    public List<VisualNovelResponse> getBestEvaluatedVisualNovels(){

        List<VisualNovelEntity> visualNovelList = visualNovelRepository.topBestEvaluated();
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
    public ResponseEntity<String> putVisualNovel(@RequestHeader HttpHeaders headers, @PathVariable @NonNull Long id, 
    @RequestBody VisualNovelBody body) throws NotFoundException, UnauthorizedException, BadRequestException{

        if(!this.isAuthorized(headers))
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
        visualNovel.setSummary(body.getSummary());
        visualNovel.setPercent_positive_evaluation_on_steam(body.getPercentPositiveEvaluationOnSteam());
        visualNovel.setNumber_evaluation_on_steam(body.getNumberEvaluationOnSteam());

        Map<String, TagEntity> tagMap = this.mapTags(body.getTags());
        List<TagEntity> tags = new ArrayList<TagEntity>();

        if(body.getTags() != null){
            for(String tagName : body.getTags())
                tags.add(tagMap.get(tagName));
        }

        visualNovel.setTags(tags);

        String dateString = body.getReleaseDate();
        Date releaseDate = Util.toDate(dateString);
        visualNovel.setRelease_date(releaseDate);
        visualNovelRepository.save(visualNovel);

        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> result = new ResponseEntity<String>("", responseHeaders, 204);

        return result;
    }


    /** Given a list of tag, this function return a map containing the TagEntity matching each tag.
     * If a tag wasn't already existing, the tag will be created in the database. */
    private Map<String, TagEntity> mapTags(List<String> tags){

        if(tags == null)
            return new HashMap<String, TagEntity>();

        Map<String, TagEntity> result = new HashMap<String, TagEntity>();
        List<TagEntity> allTags = this.tagRepository.list();

        for(String tagName: tags){
            TagEntity matching = null;

            for(TagEntity entity : allTags){
                if(entity.getTag().equals(tagName))
                    matching = entity;
            }

            if(matching == null){
                TagEntity t = new TagEntity(tagName);
                this.tagRepository.save(t);
                matching = t;
            }

            result.put(tagName, matching);
        }

        return result;
        
    }

    /** Given the headers send by the client, return true if the client is Authentificated, and false otherwise.
    * This function is to use only for urls where authentification is mandatory. By example, listing all
    * visual novel on site, without modify them, doesn't requite to be connected.*/
    private boolean isAuthorized(HttpHeaders headers){
        List<String> authorizations = headers.get("Authorization");

        if(authorizations == null || authorizations.size() == 0)
            return false;

        String[] parts = authorizations.get(0).trim().split(" ");

        if(parts.length != 2 || !"Bearer".equals(parts[0]))
            return false;

        List<UserEntity> users = tokenRepository.queryBearerByToken(parts[1]);

        if(users.size() != 1)
            return false;

        return true;
    }

}
