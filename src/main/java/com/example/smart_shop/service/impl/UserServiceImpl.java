package com.example.smart_shop.service.impl;

import com.example.smart_shop.model.Role;
import com.example.smart_shop.model.User;
import com.example.smart_shop.repository.RoleRepository;
import com.example.smart_shop.repository.UserRepository;
import com.example.smart_shop.service.UserService;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepo,
                           RoleRepository roleRepo,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public Role getOrCreateRole(String roleName) {
        Role r = roleRepo.findByName(roleName);
        if (r == null) {
            r = new Role();
            r.setName(roleName);
            r = roleRepo.save(r);
        }
        return r;
    }

    // ---------- seed default admin user ----------
    @PostConstruct
    public void initAdmin() {
        if (userRepo.findByEmail("admin@shop.com") != null) {
            return; // already exists
        }

        User admin = new User();
        admin.setEmail("admin@shop.com");
        admin.setFullName("Shop Admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEnabled(true);

        Role adminRole = getOrCreateRole("ROLE_ADMIN");
        admin.setRoles(Set.of(adminRole));

        userRepo.save(admin);
    }
}
