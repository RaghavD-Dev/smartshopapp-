package com.example.smart_shop.controller;

import com.example.smart_shop.model.Role;
import com.example.smart_shop.model.User;
import com.example.smart_shop.service.UserService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
public class AuthController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserService userService,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               @RequestParam("role") String role) {

        // If email already exists -> return error
        if (userService.findByEmail(user.getEmail()) != null) {
            return "redirect:/register?error=email";
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Determine role (USER / ADMIN)
        String roleName = role.equalsIgnoreCase("ADMIN")
                ? "ROLE_ADMIN"
                : "ROLE_USER";

        // Fetch or create role
        Role userRole = userService.getOrCreateRole(roleName);

        // Assign role
        user.setRoles(Set.of(userRole));

        // Save user
        userService.save(user);

        return "redirect:/login?registered";
    }
}
