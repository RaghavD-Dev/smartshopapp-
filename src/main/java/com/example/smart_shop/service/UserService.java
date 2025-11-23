package com.example.smart_shop.service;

import com.example.smart_shop.model.Role;
import com.example.smart_shop.model.User;

public interface UserService {

    User findByEmail(String email);

    User save(User user);

    Role getOrCreateRole(String roleName);
}
