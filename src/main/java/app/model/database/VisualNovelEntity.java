package app.model.database;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;


@Getter
@Setter
@Table( name="VISUAL_NOVEL")
@Entity
public class VisualNovelEntity {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;


    String title;

    @Column(name = "DESCRIPTION", length = -1)
    String description;

    @Column(name = "IMAGE_BASE64", length = -1)
    String image_base64;

    Date release_date;

    public VisualNovelEntity(){
        this.title = null;
        this.image_base64 = null;
        this.description = null;
    }

    public VisualNovelEntity(String title, String image_base64, String description, LocalDate release_date){
        this.title = title;
        this.image_base64 = image_base64;
        this.description = description;

        if(release_date == null)
            this.release_date = null;
        else
            this.release_date = new Date(release_date.toEpochDay());
    }
}
