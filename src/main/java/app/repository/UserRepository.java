package app.repository;


import java.util.List;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import app.model.database.UserEntity;


public interface UserRepository  extends CrudRepository<UserEntity, Integer> {
    

    @NativeQuery("SELECT * FROM registered_user WHERE pseudo = ?1")
    List<UserEntity> queryByUser(String pseudo);

    @NativeQuery("SELECT * FROM registered_user WHERE email = ?1")
    List<UserEntity> queryByEmail(String email);

    @NativeQuery("SELECT * FROM registered_user WHERE verification_link = ?1")
    List<UserEntity> queryByVerificationLink(String verificationLink);

}
