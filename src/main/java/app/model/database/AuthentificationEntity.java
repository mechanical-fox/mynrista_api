package app.model.database;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table( name="REGISTERED_TOKEN")
@Entity
public class AuthentificationEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;

    String token;

    @ManyToOne
    @JoinColumn(name = "id")
    UserEntity fk_user;

    // Ajouter Date Fin + voir comment on l'écrit... Package java.sql.Time / java.sql.Timestamp / java.sql.Date ou java.util (souvent deprecated)

    // Ajouter Constructeur defaut + Constructeur pour Créer
}
