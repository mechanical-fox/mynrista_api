package app.repository;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import app.model.database.VisualNovelEntity;

import java.util.List;

public interface VisualNovelRepository extends CrudRepository<VisualNovelEntity, Long> {
    

    @NativeQuery("SELECT * FROM visual_novel")
    public List<VisualNovelEntity> list();

    @NativeQuery("SELECT * FROM visual_novel WHERE release_date IS NOT NULL and release_date < now() ORDER BY release_date DESC LIMIT 4")
    public List<VisualNovelEntity> topNewVisualNovels();

    @NativeQuery("SELECT *" + 
        " FROM visual_novel" +
        " WHERE release_date IS NOT NULL AND percent_positive_evaluation_on_steam IS NOT NULL" +
        "    AND number_evaluation_on_steam > 10" +
        " ORDER BY percent_positive_evaluation_on_steam DESC" + 
        " LIMIT 8")
    public List<VisualNovelEntity> topBestEvaluated();



}

