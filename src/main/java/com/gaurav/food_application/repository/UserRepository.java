package com.gaurav.food_application.repository;

import com.gaurav.food_application.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    public Optional<UserEntity> findByUsername(String username);
}
