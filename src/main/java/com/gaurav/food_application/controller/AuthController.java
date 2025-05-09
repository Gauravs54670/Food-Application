package com.gaurav.food_application.controller;

import com.gaurav.food_application.io.AuthRequest;
import com.gaurav.food_application.io.AuthResponse;
import com.gaurav.food_application.service.UserService.ApplicationUserDetailService;
import com.gaurav.food_application.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ApplicationUserDetailService userDetailService;
    @Autowired
    private JwtUtils jwtUtils;
    @PostMapping("/login")
    public AuthResponse loginUser(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        UserDetails userDetails = userDetailService.loadUserByUsername(authRequest.getUsername());
        String jwtToken = jwtUtils.generateToken(userDetails);
        return new AuthResponse(userDetails.getUsername(), jwtToken);
    }
}
