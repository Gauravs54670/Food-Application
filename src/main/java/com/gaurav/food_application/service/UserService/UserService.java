package com.gaurav.food_application.service.UserService;

import com.gaurav.food_application.io.UserRequest;
import com.gaurav.food_application.io.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public UserResponse saveNewUser(UserRequest userRequest);
    public String findByUserId();
}
