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
@Table( name="REGISTERED_USER")
@Entity
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;

    String pseudo;
    String email;
    String hash_password;


    public UserEntity(){
        this.pseudo = null;
        this.email = null;
        this.hash_password = null;
    }

    public UserEntity(String pseudo, String email, String hash_password){
        this.pseudo = pseudo;
        this.email = email;
        this.hash_password = hash_password;
    }
}
