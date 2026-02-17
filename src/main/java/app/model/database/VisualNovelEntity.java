package app.model.database;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Table( name="VISUAL_NOVEL")
@Entity
public class VisualNovelEntity {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Schema(example = "1")
    Long id;

    @Schema(example = "Repurpose")
    String title;

    @Column(name = "DESCRIPTION", length = -1)
    @Schema(description = "", example = "Story\n You and several others have passed away.\n\n" +
    " Rather than being sent to Heaven or Hell, the powers that may be are unable to decide where you should go." +
    "You wake up in the Garden of Eden who s new home has been made in Limbo.")
    String description;

    @Column(name = "IMAGE_BASE64", length = -1)
    @Schema(description = "", example="data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHEl" + 
    "EQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==")
    String image_base64;

    public VisualNovelEntity(){
        this.title = null;
        this.image_base64 = null;
        this.description = null;
    }

    public VisualNovelEntity(String tile, String image_base64, String description){
        this.title = tile;
        this.image_base64 = image_base64;
        this.description = description;
    }
}
