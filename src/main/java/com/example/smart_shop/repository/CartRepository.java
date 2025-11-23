package com.example.smart_shop.repository;

import com.example.smart_shop.model.Cart;
import com.example.smart_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
