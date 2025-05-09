package com.gaurav.food_application.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
@Builder
@AllArgsConstructor
public class CartResponse {
    private String userId;
    private Map<String,Integer> item = new HashMap<>();
}
