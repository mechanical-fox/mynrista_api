package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import app.model.database.TagEntity;


public interface TagRepository extends CrudRepository<TagEntity, Long> {

    @NativeQuery("SELECT * FROM ref_tag")
    public List<TagEntity> list();
    
}
