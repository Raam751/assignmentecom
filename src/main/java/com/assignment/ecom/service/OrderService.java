package com.assignment.ecom.service;

import com.assignment.ecom.dto.CreateOrderRequest;
import com.assignment.ecom.dto.OrderResponse;
import com.assignment.ecom.model.CartItem;
import com.assignment.ecom.model.Order;
import com.assignment.ecom.model.OrderItem;
import com.assignment.ecom.model.Product;
import com.assignment.ecom.repository.OrderRepository;
import com.assignment.ecom.repository.PaymentRepository;
import com.assignment.ecom.repository.ProductRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    public Order createOrder(CreateOrderRequest request) {
        List<CartItem> cartItems = cartService.getCartItemsByUserId(request.getUserId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId()).orElse(null);
            if (product == null)
                continue;
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setId(UUID.randomUUID().toString());
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItems.add(orderItem);

            total += product.getPrice() * cartItem.getQuantity();

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setTotalAmount(total);
        order.setStatus("CREATED");
        order.setCreatedAt(Instant.now());
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(request.getUserId());

        return savedOrder;
    }

    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null)
            return null;

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setItems(order.getItems());
        response.setPayment(paymentRepository.findByOrderId(orderId));

        return response;
    }

    public Order updateOrderStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
