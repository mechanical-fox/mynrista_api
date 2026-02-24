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

import app.exception.BadRequestException;
import app.model.in.VisualNovelBody;
import app.util.Util;


@Getter
@Setter
@Table( name="VISUAL_NOVEL")
@Entity
public class VisualNovelEntity {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;


    String title;
    Date release_date;
    Integer percent_positive_evaluation_on_steam;
    Integer number_evaluation_on_steam;


    @Column(name = "SUMMARY", length = -1)
    String summary;

    @Column(name = "DESCRIPTION", length = -1)
    String description;

    @Column(name = "IMAGE_BASE64", length = -1)
    String image_base64;



    public VisualNovelEntity(){
        this.title = null;
        this.release_date = null;
        this.summary = null;
        this.percent_positive_evaluation_on_steam = null;
        this.number_evaluation_on_steam = null;
        this.description = null;
        this.image_base64 = null;
    }

    public VisualNovelEntity(VisualNovelBody visualNovel) throws BadRequestException{
        this.title = visualNovel.getTitle();
        this.release_date = Util.toDate(visualNovel.getReleaseDate());
        this.summary= visualNovel.getSummary();
        this.percent_positive_evaluation_on_steam = visualNovel.getPercentPositiveEvaluationOnSteam();
        this.number_evaluation_on_steam = visualNovel.getNumberEvaluationOnSteam();
        this.description = visualNovel.getDescription();
        this.image_base64 = visualNovel.getImage_base64();
        

        
    }
}
