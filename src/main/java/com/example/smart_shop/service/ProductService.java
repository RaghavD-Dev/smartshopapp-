package com.example.smart_shop.service;

import com.example.smart_shop.model.Product;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    List<Product> search(String keyword);
    Product saveProduct(Product product, MultipartFile imageFile);
    void deleteById(Long id);
}
