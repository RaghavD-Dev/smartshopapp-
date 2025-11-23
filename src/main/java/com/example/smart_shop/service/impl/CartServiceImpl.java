package com.example.smart_shop.service.impl;

import com.example.smart_shop.model.*;
import com.example.smart_shop.repository.*;
import com.example.smart_shop.service.CartService;
import com.example.smart_shop.service.dto.CartInsights;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final ProductRepository productRepo;

    public CartServiceImpl(CartRepository cartRepo,
                           CartItemRepository cartItemRepo,
                           ProductRepository productRepo) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.productRepo = productRepo;
    }

    @Override
    public Cart getCartForUser(User user) {
        Cart cart = cartRepo.findByUser(user);

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepo.save(cart);
        }

        return cart;
    }

    @Override
    public void addToCart(User user, Long productId, int qty) {
        Cart cart = getCartForUser(user);
        Product product = productRepo.findById(productId).orElseThrow();

        CartItem existing = cart.getItems()
                .stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + qty);
            cartItemRepo.save(existing);
            return;
        }

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(qty);
        item.setItemPrice(product.getPrice());
        item.setItemDiscountPrice(product.getDiscountPrice());

        cartItemRepo.save(item);
    }

    @Override
    public void removeItem(User user, Long itemId) {
        cartItemRepo.deleteById(itemId);
    }

    @Override
    public CartInsights getCartInsights(User user) {
        Cart cart = getCartForUser(user);
        CartInsights insights = new CartInsights();

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return insights;
        }

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal savings = BigDecimal.ZERO;
        Map<String, Integer> categoryCount = new HashMap<>();

        for (CartItem item : cart.getItems()) {

            BigDecimal usedPrice = (item.getItemDiscountPrice() != null)
                    ? item.getItemDiscountPrice()
                    : item.getItemPrice();

            total = total.add(usedPrice.multiply(BigDecimal.valueOf(item.getQuantity())));

            if (item.getItemDiscountPrice() != null) {
                BigDecimal diff = item.getItemPrice().subtract(item.getItemDiscountPrice());
                savings = savings.add(diff.multiply(BigDecimal.valueOf(item.getQuantity())));
            }

            // Category is now an object → use its name
            Category category = item.getProduct().getCategory();
            String catName = (category != null && category.getName() != null)
                    ? category.getName()
                    : "Others";

            categoryCount.put(catName, categoryCount.getOrDefault(catName, 0) + item.getQuantity());
        }

        BigDecimal avg = total.divide(
                BigDecimal.valueOf(cart.getItems().size()),
                2,
                RoundingMode.HALF_UP
        );

        insights.setAveragePrice(avg);
        insights.setTotalSavings(savings);

        String dominant = categoryCount.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        insights.setDominantCategory(dominant);

        return insights;
    }

    @Override
    public void clearCart(User user) {
        Cart cart = cartRepo.findByUser(user);
        if (cart == null) return;

        cartItemRepo.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepo.save(cart);
    }
}
