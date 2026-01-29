package app.model.database;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table( name="REGISTERED_TOKEN")
@Entity
public class TokenEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;
    String token;

    @OneToOne
    @JoinColumn(name = "id")
    UserEntity fk_user;

    Timestamp expiration_date;

    public TokenEntity(String token, UserEntity fk_user, LocalDateTime expiration_date){
        this.token = token;
        this.fk_user = fk_user;

        // Warning: It's returned second, and not millisecond. 
        // This create easilly an error
        long millisecondsSinceEpoch = expiration_date.toEpochSecond(ZoneOffset.UTC) * 1000;
        this.expiration_date = new Timestamp(millisecondsSinceEpoch);
    }

    public TokenEntity(){
        this.token = null;
        this.fk_user = null;
        this.expiration_date = null;
    }

}
