package com.gaurav.food_application.service.CartService;

import com.gaurav.food_application.io.CartRequest;
import com.gaurav.food_application.io.CartResponse;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    public CartResponse addToCart(CartRequest cartRequest);
    public CartResponse getCart();
    public void clearCart();
    public CartResponse removeQuantity(CartRequest cartRequest);
}
