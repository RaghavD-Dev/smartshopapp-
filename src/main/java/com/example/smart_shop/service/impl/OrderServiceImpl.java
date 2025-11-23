package com.example.smart_shop.service.impl;

import com.example.smart_shop.model.Order;
import com.example.smart_shop.repository.OrderRepository;
import com.example.smart_shop.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;

    public OrderServiceImpl(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Override
    public List<Order> findAll() {
        return orderRepo.findAll();
    }
}
