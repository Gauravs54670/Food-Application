package com.gaurav.food_application.controller;

import com.gaurav.food_application.io.UserRequest;
import com.gaurav.food_application.io.UserResponse;
import com.gaurav.food_application.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest){
        try {
            System.out.println("Request processed");
            UserResponse userResponse = this.userService.saveNewUser(userRequest);
            Map<String, Object> map = new HashMap<>();
            map.put("message","User registered successfully");
            map.put("response",userResponse);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
