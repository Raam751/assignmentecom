package com.assignment.ecom.service;

import com.assignment.ecom.model.Product;
import com.assignment.ecom.repository.ProductRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // simple CRUD operations for products
    
    public Product createProduct(Product product) {
        // could add validation here later
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        // might need pagination if we get too many products
        return productRepository.findAll();
    }

    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }
}
