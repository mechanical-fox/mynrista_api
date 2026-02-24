package app.model.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import app.model.database.TagEntity;
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

    @Schema(description = "", example = "\"27 déc. 2023\"")
    String textReleaseDate;

    @Schema(description = "", example = "You and several others have passed away. You have three options: either take" + 
    "the trials to earn your place in Heaven, live it up in Hell, or remain lost in Limbo for the rest of eternity.")
    String summary;

    @Schema(description = "", example = "91")
    Integer percentPositiveEvaluationOnSteam;

    @Schema(description = "", example = "59")
    Integer numberEvaluationOnSteam;

    @Schema(description = "", example = "Story\n You and several others have passed away.\n\n" +
    " Rather than being sent to Heaven or Hell, the powers that may be are unable to decide where you should go." +
    "You wake up in the Garden of Eden who s new home has been made in Limbo.")
    String description;

    @Schema(description = "", example = "[\"Casual\", \"Fantasy\", \"Roman graphique\"]")
    List<String> tags;

    @Schema(description = "", example="data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHEl" + 
    "EQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==")
    String image_base64;



    public VisualNovelResponse(){

        this.title = null;
        this.releaseDate = null;
        this.textReleaseDate = null;
        this.summary = null;
        this.percentPositiveEvaluationOnSteam = null;
        this.numberEvaluationOnSteam = null;
        this.description = null;
        this.tags = null;
        this.image_base64 = null;
        
    }

    public VisualNovelResponse(VisualNovelEntity visualNovel){

        this.id = visualNovel.getId();
        this.title = visualNovel.getTitle();
        this.summary = visualNovel.getSummary();
        this.percentPositiveEvaluationOnSteam = visualNovel.getPercent_positive_evaluation_on_steam();
        this.numberEvaluationOnSteam = visualNovel.getNumber_evaluation_on_steam();
        this.tags = new ArrayList<String>();
        this.description = visualNovel.getDescription();
        this.image_base64 = visualNovel.getImage_base64();

        for(TagEntity tag : visualNovel.getTags())
            this.tags.add(tag.getTag());

        if(visualNovel.getRelease_date() == null)
            this.releaseDate = null;
        else
            this.releaseDate = Util.toString(visualNovel.getRelease_date());

        if(this.releaseDate == null)
            this.textReleaseDate = "A déterminer";
        else{
            String[] parts = this.releaseDate.split("/");
            Integer indMonth = Integer.valueOf(parts[1]) - 1;
            String[] months = {"jan.", "fév.", "mars", "avr.", "mai", "juin", "juil.", "août", "sept.", "oct.", "nov.", "déc."};
            this.textReleaseDate = parts[0] + " " + months[indMonth] + " " + parts[2];
        }
    }
}
