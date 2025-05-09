package com.gaurav.food_application.service.foodservice;

import com.gaurav.food_application.io.FoodRequest;
import com.gaurav.food_application.io.FoodResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface FoodService {
    public String uploadFile(MultipartFile file);
    public FoodResponse addFood(FoodRequest foodRequest, MultipartFile file);
    public List<FoodResponse> getFoodList();
    public FoodResponse getFood(String foodId);
    public boolean deleteFile(String fileName);
    public void deleteFood(String foodId);
}
