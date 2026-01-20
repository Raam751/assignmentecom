package com.assignment.ecom.controller;

import com.assignment.ecom.model.Product;
import com.assignment.ecom.service.ProductService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        // add new product to catalog
        return productService.createProduct(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        // returns all products in the system
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }
}
