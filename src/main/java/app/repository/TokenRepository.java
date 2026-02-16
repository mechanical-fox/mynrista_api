package app.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import app.model.database.TokenEntity;
import jakarta.transaction.Transactional;

public interface TokenRepository extends CrudRepository<TokenEntity, Integer> {
    
    @Modifying
    @Transactional
    @NativeQuery("DELETE FROM registered_token WHERE expiration_date < NOW()")
    void deleteExpiredTokens();
}
