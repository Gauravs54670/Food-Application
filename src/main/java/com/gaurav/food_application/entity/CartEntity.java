package com.gaurav.food_application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cart")
public class CartEntity {
    @Id
    private String id;
    private String userId;
    private Map<String, Integer> items = new HashMap<>();
    public CartEntity(String userId, Map<String, Integer> map){
        this.userId = userId;
        this.items = map;
    }
}
