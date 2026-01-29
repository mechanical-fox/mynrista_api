package app.model.out;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {


    @Schema(example = "1171660c4f41406d")
    String token;
    @Schema(example = "3600")
    long expireIn;
    @Schema(example = "Bearer Authentication")
    String authorizationType;



    public TokenResponse(String token, long expireIn){
        this.token = token;
        this.expireIn = expireIn;
        this.authorizationType = "Bearer Authentication";
    }
}
