package app.repository;

import org.springframework.data.repository.CrudRepository;

import app.model.database.VisualNovelEntity;

public interface VisualNovelRepository extends CrudRepository<VisualNovelEntity, Long> {
    
}
