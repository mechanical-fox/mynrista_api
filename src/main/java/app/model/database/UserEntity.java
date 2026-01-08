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
@Table( name="MYN_USER")
@Entity
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;

    String pseudo;
    String email;
    String hash_password;
    Boolean isEmailChecked;


    public UserEntity(){
        this.pseudo = null;
        this.email = null;
        this.hash_password = null;
        this.isEmailChecked = null;
    }

    public UserEntity(String pseudo, String email, String hash_password, Boolean isEmailChecked){
        this.pseudo = pseudo;
        this.email = email;
        this.hash_password = hash_password;
        this.isEmailChecked = isEmailChecked;
    }
}
