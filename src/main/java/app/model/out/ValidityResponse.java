package app.model.out;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidityResponse {
    
    @Schema(example = "false")
    Boolean isRegistrationAccepted;
    @Schema(example = "true")
    Boolean pseudoAlreadyExisting;
    @Schema(example = "false")
    Boolean emailAlreadyExisting;
    @Schema(example = "[\"Les mots de passe doivent avoir une longueur >= 6\"]")
    List<String> othersRules;

    public ValidityResponse(Boolean isRegistrationAccepted, Boolean pseudoAlreadyExisting, 
    Boolean emailAlreadyExisting, List<String> othersRules){
        this.isRegistrationAccepted = isRegistrationAccepted;
        this.pseudoAlreadyExisting = pseudoAlreadyExisting;
        this.emailAlreadyExisting = emailAlreadyExisting;
        this.othersRules = othersRules;
    }

}
