package com.assignment.ecom.dto;

import lombok.Data;
import com.assignment.ecom.model.OrderItem;
import com.assignment.ecom.model.Payment;
import java.util.List;

@Data
public class OrderResponse {
    private String id;
    private String userId;
    private Double totalAmount;
    private String status;
    private List<OrderItem> items;
    private Payment payment;
}
