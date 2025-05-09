package com.gaurav.food_application.service.CartService;

import com.gaurav.food_application.entity.CartEntity;
import com.gaurav.food_application.io.CartRequest;
import com.gaurav.food_application.io.CartResponse;
import com.gaurav.food_application.repository.CartRepository;
import com.gaurav.food_application.service.AuthService.AuthenticationService;
import com.gaurav.food_application.service.UserService.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Slf4j
@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AuthenticationService authenticationService;
    @Override
    public CartResponse addToCart(CartRequest cartRequest) {
        String loggedInUserid = userService.findByUserId();
        Optional<CartEntity> existCart = this.cartRepository.findByUserId(loggedInUserid);
        CartEntity cartEntity = existCart.orElseGet(() -> new CartEntity(loggedInUserid,new HashMap<>()));
        Map<String,Integer> cartItem = cartEntity.getItems();
        cartItem.put(cartRequest.getFoodId(),cartItem.getOrDefault(cartRequest.getFoodId(),0)+1);
        //        log.info("Response is {}",cartResponse);
        return convertToResponse(this.cartRepository.save(cartEntity));
    }

    @Override
    public CartResponse getCart() {
        String userId = this.userService.findByUserId();
        CartEntity cartEntity = this.cartRepository.findByUserId(userId)
                .orElseGet(() -> new CartEntity(userId,new HashMap<>()));
        return convertToResponse(cartEntity);
    }

    @Override
    public void clearCart() {
        String userId = this.userService.findByUserId();
        CartEntity cartEntity = this.cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No cart found"));
        this.cartRepository.delete(cartEntity);
    }

    @Override
    public CartResponse removeQuantity(CartRequest cartRequest) {
        String loggedInUserId = this.userService.findByUserId();
        CartEntity cartEntity = this.cartRepository.findByUserId(loggedInUserId)
                .orElseThrow(() -> new RuntimeException("No cart found"));
        Map<String,Integer> itemMap = cartEntity.getItems();
        if(!itemMap.containsKey(cartRequest.getFoodId()))
                throw new RuntimeException("Item not found in cart");
        itemMap.computeIfPresent(cartRequest.getFoodId(),(k, v) -> (v > 1) ? v - 1 : null);
        cartEntity.setItems(itemMap);
        return convertToResponse(this.cartRepository.save(cartEntity));
    }

    private CartResponse convertToResponse(CartEntity cartEntity){
        return CartResponse.builder()
                .userId(cartEntity.getUserId())
                .item(cartEntity.getItems())
                .build();
    }
}
