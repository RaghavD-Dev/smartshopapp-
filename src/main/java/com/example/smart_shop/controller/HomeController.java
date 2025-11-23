package com.example.smart_shop.controller;

import com.example.smart_shop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(@RequestParam(value = "q", required = false) String keyword,
                       Model model) {

        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("products", productService.search(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("products", productService.findAll());
        }

        return "home";
    }
}
