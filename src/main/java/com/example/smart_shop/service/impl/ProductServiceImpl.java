package com.example.smart_shop.service.impl;

import com.example.smart_shop.model.Product;
import com.example.smart_shop.repository.ProductRepository;
import com.example.smart_shop.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;

    public ProductServiceImpl(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public List<Product> findAll() {
        return productRepo.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    @Override
    public List<Product> search(String keyword) {
        return productRepo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Product saveProduct(Product product, MultipartFile imageFile) {

        try {
            // If NEW image uploaded
            if (imageFile != null && !imageFile.isEmpty()) {
                Path uploadDir = Paths.get("uploads");
                Files.createDirectories(uploadDir);

                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path filePath = uploadDir.resolve(fileName);

                Files.write(filePath, imageFile.getBytes());
                product.setImageUrl("/uploads/" + fileName);

            } else if (product.getId() != null) {
                // EDIT MODE: keep old image if no new file
                Product existing = productRepo.findById(product.getId()).orElse(null);
                if (existing != null) {
                    product.setImageUrl(existing.getImageUrl());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed!", e);
        }

        // JPA will UPDATE when id != null, INSERT when id == null
        return productRepo.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepo.deleteById(id);
    }
}
