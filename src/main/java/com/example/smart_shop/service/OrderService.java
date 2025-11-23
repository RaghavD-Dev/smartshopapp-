package com.example.smart_shop.service;

import com.example.smart_shop.model.Order;
import java.util.List;

public interface OrderService {
    List<Order> findAll();
}
