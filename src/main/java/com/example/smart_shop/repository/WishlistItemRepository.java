package com.example.smart_shop.repository;

import com.example.smart_shop.model.User;
import com.example.smart_shop.model.Product;
import com.example.smart_shop.model.WishlistItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

    List<WishlistItem> findByUser(User user);

    boolean existsByUserAndProduct(User user, Product product);  // <-- ADD THIS
}
