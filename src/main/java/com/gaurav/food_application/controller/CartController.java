package com.gaurav.food_application.controller;

import com.gaurav.food_application.io.CartRequest;
import com.gaurav.food_application.io.CartResponse;
import com.gaurav.food_application.service.CartService.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody CartRequest cartRequest) {
        if(cartRequest.getFoodId() == null || cartRequest.getFoodId().isEmpty())
            throw new RuntimeException("No food entity id found");
        try {
            CartResponse cartResponse = this.cartService.addToCart(cartRequest);
            return new ResponseEntity<>(cartResponse, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("message","error in service");
            map.put("error",e.getMessage());
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping
    public ResponseEntity<?> getCart() {
        try {
            CartResponse cartResponse = this.cartService.getCart();
            Map<String, Object> map = new HashMap<>();
            map.put("message","cart fetched successfully");
            map.put("response",cartResponse);
            return new ResponseEntity<>(map,HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("message","error in getting cart");
            map.put("error",e.getMessage());
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping
    public ResponseEntity<?> deleteCart(){
        try {
            this.cartService.clearCart();
            Map<String, Object> map = new HashMap<>();
            map.put("message","cart deleted successfully");
            return new ResponseEntity<>(map,HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("message","error in getting cart");
            map.put("error",e.getMessage());
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/remove")
    public ResponseEntity<?> reduceQuantity(@RequestBody CartRequest cartRequest) {
        try {
            if(cartRequest.getFoodId() == null || cartRequest.getFoodId().isEmpty())
                throw new RuntimeException("no request found");
            CartResponse cartResponse = this.cartService.removeQuantity(cartRequest);
            Map<String, Object> map = new HashMap<>();
            map.put("response",cartResponse);
            return new ResponseEntity<>(map,HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("message","error in getting cart");
            map.put("error",e.getMessage());
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }
    }
}
