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
@Table( name="REF_TAG")
@Entity
public class TagEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;

    String tag;

    public TagEntity(){
        this.tag = null;
    }

    public TagEntity(String tag){
        this.tag = tag;
    }
}
