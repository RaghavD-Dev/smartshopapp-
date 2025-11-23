package com.example.smart_shop.service;

import com.example.smart_shop.model.Cart;
import com.example.smart_shop.model.User;
import com.example.smart_shop.service.dto.CartInsights;

public interface CartService {

    Cart getCartForUser(User user);

    void addToCart(User user, Long productId, int quantity);

    void removeItem(User user, Long cartItemId);

    CartInsights getCartInsights(User user);

    // ✅ Added for CheckoutController
    void clearCart(User user);
}
