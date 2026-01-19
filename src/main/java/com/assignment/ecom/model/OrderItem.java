package com.assignment.ecom.model;

import lombok.Data;

@Data
public class OrderItem {
    private String id;
    private String productId;
    private Integer quantity;
    private Double price;
}
