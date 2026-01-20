package com.assignment.ecom.controller;

import com.assignment.ecom.dto.CreateOrderRequest;
import com.assignment.ecom.dto.OrderResponse;
import com.assignment.ecom.model.Order;
import com.assignment.ecom.service.OrderService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public Order createOrder(@RequestBody CreateOrderRequest request) {
        // creates order from user's cart
        return orderService.createOrder(request);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable String userId) {
        // get all orders for a specific user
        return orderService.getOrdersByUserId(userId);
    }
}
