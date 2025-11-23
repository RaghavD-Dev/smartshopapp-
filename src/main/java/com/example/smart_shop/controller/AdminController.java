package com.example.smart_shop.controller;

import com.example.smart_shop.service.CategoryService;
import com.example.smart_shop.service.ProductService;
import com.example.smart_shop.service.OrderService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderService orderService;

    public AdminController(CategoryService categoryService,
                           ProductService productService,
                           OrderService orderService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("products", productService.findAll());
        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("categories", categoryService.findAll());

        return "admin/dashboard";
    }
}
