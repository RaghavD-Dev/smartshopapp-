package com.example.smart_shop.controller;

import com.example.smart_shop.model.*;
import com.example.smart_shop.repository.OrderRepository;
import com.example.smart_shop.service.CartService;
import com.example.smart_shop.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CheckoutController {

    private final UserService userService;
    private final CartService cartService;
    private final OrderRepository orderRepo;

    public CheckoutController(UserService userService,
                              CartService cartService,
                              OrderRepository orderRepo) {
        this.userService = userService;
        this.cartService = cartService;
        this.orderRepo = orderRepo;
    }

    // -------------------------
    // Helper: Get logged user
    // -------------------------
    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) return null;
        if ("anonymousUser".equals(auth.getPrincipal())) return null;

        return auth.getName(); // username = email
    }

    // -------------------------
    // SHOW CHECKOUT PAGE
    // -------------------------
    @GetMapping("/checkout")
    public String checkoutPage(Model model) {

        String email = getLoggedInUserEmail();
        if (email == null) return "redirect:/login";

        User user = userService.findByEmail(email);
        Cart cart = cartService.getCartForUser(user);

        model.addAttribute("cart", cart);
        return "checkout";
    }

    // -------------------------
    // PLACE ORDER
    // -------------------------
    @PostMapping("/checkout")
    public String placeOrder(@RequestParam(required = false) String orderNotes) {

        String email = getLoggedInUserEmail();
        if (email == null) return "redirect:/login";

        User user = userService.findByEmail(email);
        Cart cart = cartService.getCartForUser(user);

        // no items -> go to cart
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return "redirect:/cart?empty";
        }

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");
        order.setNotes(orderNotes);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem ci : cart.getItems()) {

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            oi.setItemPrice(ci.getItemPrice());
            oi.setItemDiscountPrice(ci.getItemDiscountPrice());

            orderItems.add(oi);
        }

        order.setItems(orderItems);

        // Save order + items
        orderRepo.save(order);

        // Clear cart after placing the order
        cartService.clearCart(user);

        return "redirect:/?orderSuccess";
    }
}
