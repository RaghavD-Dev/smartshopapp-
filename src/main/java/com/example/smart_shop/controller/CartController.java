package com.example.smart_shop.controller;

import com.example.smart_shop.model.Cart;
import com.example.smart_shop.model.CartItem;
import com.example.smart_shop.model.User;
import com.example.smart_shop.service.CartService;
import com.example.smart_shop.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService,
                          UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    // -------------------------------
    // GET LOGGED-IN USER EMAIL
    // -------------------------------
    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        return auth.getName(); // email is username
    }

    // -------------------------------
    // VIEW CART
    // -------------------------------
    @GetMapping
    public String viewCart(Model model) {

        String email = getLoggedInUserEmail();
        if (email == null) return "redirect:/login";

        User user = userService.findByEmail(email);
        Cart cart = cartService.getCartForUser(user);

        // ---- Calculate Total ----
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {

            BigDecimal price = item.getItemDiscountPrice() != null
                    ? item.getItemDiscountPrice()
                    : item.getItemPrice();

            total = total.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        model.addAttribute("cart", cart);
        model.addAttribute("cartTotal", total);

        return "cart";
    }

    // -------------------------------
    // ADD TO CART
    // -------------------------------
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity) {

        String email = getLoggedInUserEmail();
        if (email == null) return "redirect:/login";

        User user = userService.findByEmail(email);

        cartService.addToCart(user, productId, quantity);

        return "redirect:/cart?added";
    }

    // -------------------------------
    // REMOVE ITEM
    // -------------------------------
    @PostMapping("/remove")
    public String removeItem(@RequestParam Long itemId) {

        String email = getLoggedInUserEmail();
        if (email == null) return "redirect:/login";

        User user = userService.findByEmail(email);
        cartService.removeItem(user, itemId);

        return "redirect:/cart?removed";
    }

    // -------------------------------
    // CART ANALYTICS
    // -------------------------------
    @GetMapping("/insights")
    public String cartInsights(Model model) {

        String email = getLoggedInUserEmail();
        if (email == null) return "redirect:/login";

        User user = userService.findByEmail(email);
        model.addAttribute("insights", cartService.getCartInsights(user));

        return "cart-insights";
    }
}
