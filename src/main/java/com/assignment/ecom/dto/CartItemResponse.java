package com.assignment.ecom.dto;

import lombok.Data;
import com.assignment.ecom.model.Product;

@Data
public class CartItemResponse {
    private String id;
    private String productId;
    private Integer quantity;
    private Product product;
}
