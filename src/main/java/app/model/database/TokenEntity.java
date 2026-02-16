package app.model.database;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table( name="REGISTERED_TOKEN")
@Entity
public class TokenEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;
    String token;

    @ManyToOne
    @JoinColumn(name = "fk_user")
    UserEntity user;

    Timestamp expiration_date;

    public TokenEntity(String token, UserEntity user, OffsetDateTime expiration_date){
        this.token = token;
        this.user = user;

        // Warning: Beetween the functions it's necessary to convert beetween seconds and milliseconds
        long millisecondsSinceEpoch = expiration_date.toEpochSecond() * 1000;
        this.expiration_date = new Timestamp(millisecondsSinceEpoch);
    }

    public TokenEntity(){
        this.token = null;
        this.user = null;
        this.expiration_date = null;
    }

}
