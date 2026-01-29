package app.repository;

import org.springframework.data.repository.CrudRepository;

import app.model.database.TokenEntity;

public interface TokenRepository extends CrudRepository<TokenEntity, Integer> {
    
}
