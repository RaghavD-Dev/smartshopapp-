package com.example.smart_shop.service;

import com.example.smart_shop.model.Category;
import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category findById(Long id); 

    void delete(Long id);
}
