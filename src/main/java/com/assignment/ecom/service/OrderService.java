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
        // grab everything from user's cart
        List<CartItem> userCart = cartService.getCartItemsByUserId(request.getUserId());
        
        if (userCart.isEmpty()) {
            throw new RuntimeException("Oops! Your cart is empty. Add some items first!");
        }

        List<OrderItem> itemsForOrder = new ArrayList<>();
        double runningTotal = 0;

        // loop through cart and build order
        for (CartItem itemInCart : userCart) {
            Product productInfo = productRepository.findById(itemInCart.getProductId()).orElse(null);
            
            if (productInfo == null)
                continue; // skip if product doesn't exist anymore
                
            // make sure we have enough stock - learned this the hard way
            if (productInfo.getStock() < itemInCart.getQuantity()) {
                throw new RuntimeException("Not enough stock for: " + productInfo.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setId(UUID.randomUUID().toString());
            orderItem.setProductId(productInfo.getId());
            orderItem.setQuantity(itemInCart.getQuantity());
            orderItem.setPrice(productInfo.getPrice());  // lock in current price
            itemsForOrder.add(orderItem);

            runningTotal += productInfo.getPrice() * itemInCart.getQuantity();

            // reduce stock immediately
            productInfo.setStock(productInfo.getStock() - itemInCart.getQuantity());
            productRepository.save(productInfo);
        }

        Order newOrder = new Order();
        newOrder.setUserId(request.getUserId());
        newOrder.setTotalAmount(runningTotal);
        newOrder.setStatus("CREATED");
        newOrder.setCreatedAt(Instant.now());
        newOrder.setItems(itemsForOrder);

        Order savedOrder = orderRepository.save(newOrder);
        
        // cart served its purpose, clear it out
        cartService.clearCart(request.getUserId());

        return savedOrder;
    }

    public OrderResponse getOrderById(String orderId) {
        Order existingOrder = orderRepository.findById(orderId).orElse(null);
        
        if (existingOrder == null)
            return null;

        // build response with payment info too
        OrderResponse orderResp = new OrderResponse();
        orderResp.setId(existingOrder.getId());
        orderResp.setUserId(existingOrder.getUserId());
        orderResp.setTotalAmount(existingOrder.getTotalAmount());
        orderResp.setStatus(existingOrder.getStatus());
        orderResp.setItems(existingOrder.getItems());
        orderResp.setPayment(paymentRepository.findByOrderId(orderId));

        return orderResp;
    }

    // webhook calls this to update status
    public Order updateOrderStatus(String orderId, String status) {
        Order orderToUpdate = orderRepository.findById(orderId).orElse(null);
        if (orderToUpdate != null) {
            orderToUpdate.setStatus(status);
            return orderRepository.save(orderToUpdate);
        }
        return null;
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
