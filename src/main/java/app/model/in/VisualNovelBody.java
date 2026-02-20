package app.model.in;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisualNovelBody {

    @Schema(description = "", example = "Repurpose")
    String title;

    @Schema(description = "", example = "27/12/2023")
    String releaseDate;

    @Schema(description = "", example = "91")
    Integer percentPositiveEvaluationOnSteam;

    @Schema(description = "", example = "59")
    Integer numberEvaluationOnSteam;

    @Schema(description = "", example = "Story\n You and several others have passed away.\n\n" +
    " Rather than being sent to Heaven or Hell, the powers that may be are unable to decide where you should go." +
    "You wake up in the Garden of Eden who s new home has been made in Limbo.")
    String description;

    @Schema(description = "", example="data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHEl" + 
    "EQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==")
    String image_base64;



    public VisualNovelBody(){
        this.title = null;
        this.releaseDate = null;
        this.percentPositiveEvaluationOnSteam = null;
        this.numberEvaluationOnSteam = null;
        this.description = null;
        this.image_base64 = null;
    }
    
}
