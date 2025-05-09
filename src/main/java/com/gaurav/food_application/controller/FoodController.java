package com.gaurav.food_application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaurav.food_application.io.FoodRequest;
import com.gaurav.food_application.io.FoodResponse;
import com.gaurav.food_application.service.foodservice.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }
    //post food
    @PostMapping
    public ResponseEntity<?> postFood(@RequestPart("food") String food,
                                      @RequestPart("file") MultipartFile file){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FoodRequest foodRequest = null;
            try{
                foodRequest = objectMapper.readValue(food,FoodRequest.class);
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
            }
            FoodResponse foodResponse = this.foodService.addFood(foodRequest,file);
            log.info("Food uploaded with food id {}",foodResponse.getId());
            return new ResponseEntity<>(foodResponse,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllFood(){
        try {
            List<FoodResponse> foodResponseList = this.foodService.getFoodList();
            return new ResponseEntity<>(foodResponseList,HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{foodId}")
    public ResponseEntity<?> getFood(@PathVariable(value = "foodId") String foodId) {
        try {
            System.out.println("request processed");
            FoodResponse foodResponse = this.foodService.getFood(foodId);
            return new ResponseEntity<>(foodResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    //delete food by id
    @DeleteMapping("/{foodId}")
    public ResponseEntity<?> deleteFood(@PathVariable("foodId") String foodId){
        try {
            System.out.println("request processed");
            this.foodService.deleteFood(foodId);
            return new ResponseEntity<>("food deleted successfully",HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"error in deleting id");
        }
    }
}
