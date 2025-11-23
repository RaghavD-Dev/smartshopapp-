package com.example.smart_shop.service.impl;

import com.example.smart_shop.model.Category;
import com.example.smart_shop.repository.CategoryRepository;
import com.example.smart_shop.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;

    public CategoryServiceImpl(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepo.findById(id).orElse(null);
    }

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}
}
