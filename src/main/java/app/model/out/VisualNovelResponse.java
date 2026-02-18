package app.model.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


import app.model.database.VisualNovelEntity;
import app.util.Util;


@Getter
@Setter
public class VisualNovelResponse {


    @Schema(example = "1")
    Long id;

    @Schema(example = "Repurpose")
    String title;

    @Schema(description = "", example = "27/12/2023")
    String releaseDate;

    @Schema(description = "", example = "Story\n You and several others have passed away.\n\n" +
    " Rather than being sent to Heaven or Hell, the powers that may be are unable to decide where you should go." +
    "You wake up in the Garden of Eden who s new home has been made in Limbo.")
    String description;

    @Schema(description = "", example="data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHEl" + 
    "EQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==")
    String image_base64;



    public VisualNovelResponse(){
        this.title = null;
        this.image_base64 = null;
        this.description = null;
    }

    public VisualNovelResponse(VisualNovelEntity visualNovel){
        this.id = visualNovel.getId();
        this.title = visualNovel.getTitle();
        this.image_base64 = visualNovel.getImage_base64();
        this.description = visualNovel.getDescription();

        if(visualNovel.getRelease_date() == null)
            this.releaseDate = null;
        else
            this.releaseDate = Util.toString(visualNovel.getRelease_date());
    }
}
