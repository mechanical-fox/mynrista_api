package app.repository;


import java.util.List;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import app.model.database.UserEntity;


public interface UserRepository  extends CrudRepository<UserEntity, Integer> {
    

    @NativeQuery("select * from MYN_USER where pseudo = ?1")
    List<UserEntity> queryByUser(String pseudo);

    @NativeQuery("select * from MYN_USER where email = ?1")
    List<UserEntity> queryByEmail(String email);

}
