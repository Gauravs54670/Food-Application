package com.gaurav.food_application.service.UserService;

import com.gaurav.food_application.entity.UserEntity;
import com.gaurav.food_application.io.UserRequest;
import com.gaurav.food_application.io.UserResponse;
import com.gaurav.food_application.repository.UserRepository;
import com.gaurav.food_application.service.AuthService.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationService authenticationService;
    @Override
    public UserResponse saveNewUser(UserRequest userRequest) {
        Optional<UserEntity> existUser = this.userRepository.findByUsername(userRequest.getEmail());
        if(existUser.isPresent()){
            log.info("user is already present with username {}",existUser.get().getUsername());
            throw new RuntimeException("Username is already taken. Please try another one.");
        }
        UserEntity newUser = convertToEntity(userRequest);
        UserResponse userResponse = convertToResponse(this.userRepository.save(newUser));
        return userResponse;
    }

    @Override
    public String findByUserId() {
        String loggedInUsername = authenticationService.getEmail();
        UserEntity user = this.userRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    private UserEntity convertToEntity(UserRequest userRequest){
        return UserEntity.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();
    }
    private UserResponse convertToResponse(UserEntity userEntity){
        return UserResponse.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .build();
    }
}
