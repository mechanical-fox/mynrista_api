package app.repository;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import app.model.database.VisualNovelEntity;

import java.util.List;

public interface VisualNovelRepository extends CrudRepository<VisualNovelEntity, Long> {
    

    @NativeQuery("SELECT * FROM visual_novel")
    public List<VisualNovelEntity> list();

    @NativeQuery("SELECT * FROM visual_novel WHERE release_date IS NOT NULL and release_date < now() ORDER BY release_date DESC")
    public List<VisualNovelEntity> topNewVisualNovels();
}

