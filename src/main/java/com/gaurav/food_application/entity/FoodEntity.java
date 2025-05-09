package com.gaurav.food_application.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "food")
public class FoodEntity {
    @Id
    private String id;
    private String name;
    private String description;
    private String category;
    private double price;
    private String imageURL;
}
