package com.example.smart_shop.controller;

import com.example.smart_shop.model.*;
import com.example.smart_shop.repository.ProductRepository;
import com.example.smart_shop.repository.WishlistItemRepository;
import com.example.smart_shop.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistItemRepository wishlistRepo;
    private final ProductRepository productRepo;
    private final UserService userService;

    public WishlistController(WishlistItemRepository wishlistRepo,
                              ProductRepository productRepo,
                              UserService userService) {
        this.wishlistRepo = wishlistRepo;
        this.productRepo = productRepo;
        this.userService = userService;
    }

    // -------------------------
    // Helper: Get logged-in email
    // -------------------------
    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        return auth.getName();
    }

    // -------------------------
    // VIEW WISHLIST
    // -------------------------
    @GetMapping
    public String viewWishlist(Model model) {

        String email = getLoggedInUserEmail();
        if (email == null) return "redirect:/login";

        User user = userService.findByEmail(email);

        model.addAttribute("items", wishlistRepo.findByUser(user));
        return "wishlist";
    }

    // -------------------------
    // ADD TO WISHLIST
    // -------------------------
    @PostMapping("/add")
    public String addToWishlist(@RequestParam Long productId,
                                @RequestParam(defaultValue = "false") boolean priceDropAlert,
                                @RequestParam(defaultValue = "false") boolean purchaseGoal) {

        String email = getLoggedInUserEmail();
        if (email == null) return "redirect:/login";

        User user = userService.findByEmail(email);
        Product product = productRepo.findById(productId)
                                     .orElseThrow(() -> new RuntimeException("Product not found"));

        // Prevent duplicate wishlist entries
        if (wishlistRepo.existsByUserAndProduct(user, product)) {
            return "redirect:/wishlist?exists";
        }

        WishlistItem item = new WishlistItem();
        item.setUser(user);
        item.setProduct(product);
        item.setPriceDropAlert(priceDropAlert);
        item.setPurchaseGoal(purchaseGoal);

        wishlistRepo.save(item);

        return "redirect:/wishlist?added";
    }
}
