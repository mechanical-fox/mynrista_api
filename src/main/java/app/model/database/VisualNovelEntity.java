package app.model.database;

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
    Long id;

    String title;
    String image_base64;
    String description;

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
