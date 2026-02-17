package app.repository;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import app.model.database.VisualNovelEntity;

import java.util.List;

public interface VisualNovelRepository extends CrudRepository<VisualNovelEntity, Long> {
    

    @NativeQuery("SELECT * FROM visual_novel")
    public List<VisualNovelEntity> list();
}
