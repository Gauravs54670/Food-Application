package com.gaurav.food_application.repository;

import com.gaurav.food_application.entity.CartEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<CartEntity, String> {
    public Optional<CartEntity> findByUserId(String userId);
    public void deleteByUserId(String userId);
}
