package app.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import app.model.database.TokenEntity;
import app.model.database.UserEntity;
import jakarta.transaction.Transactional;

import java.util.List;

public interface TokenRepository extends CrudRepository<TokenEntity, Integer> {
    
    @Modifying
    @Transactional
    @NativeQuery("DELETE FROM registered_token WHERE expiration_date < NOW()")
    void deleteExpiredTokens();

    @NativeQuery("SELECT ru.* FROM registered_token rt INNER JOIN registered_user ru ON rt.fk_user = ru.id" + 
    " WHERE rt.token = ?1 and rt.expiration_date > NOW();")
    List<UserEntity> queryBearerByToken(String token);
}
