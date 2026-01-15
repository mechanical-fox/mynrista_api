package app.model.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthentificationBody {

    @Schema(description = "", example = "itsuki@gmail.com")
    String email;
    @Schema(description = "", example = "kol7436")
    String password;

    public AuthentificationBody(){
        this.email = null;
        this.password = null;
    }
}
