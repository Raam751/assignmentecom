package com.assignment.ecom.service;

import com.assignment.ecom.client.MockPaymentClient;
import com.assignment.ecom.dto.PaymentRequest;
import com.assignment.ecom.model.Order;
import com.assignment.ecom.model.Payment;
import com.assignment.ecom.repository.OrderRepository;
import com.assignment.ecom.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final MockPaymentClient mockPaymentClient;

    public Payment createPayment(PaymentRequest request) {
        // verify order exists first
        Order orderForPayment = orderRepository.findById(request.getOrderId()).orElse(null);
        
        if (orderForPayment == null) {
            throw new RuntimeException("Order not found - can't process payment!");
        }
        
        if (!"CREATED".equals(orderForPayment.getStatus())) {
            throw new RuntimeException("Order status is " + orderForPayment.getStatus() + ", should be CREATED");
        }

        // create payment record
        Payment newPayment = new Payment();
        newPayment.setOrderId(request.getOrderId());
        newPayment.setAmount(request.getAmount());
        newPayment.setStatus("PENDING");
        
        // generate payment ID - hacky but works for now
        newPayment.setPaymentId("pay_" + UUID.randomUUID().toString().substring(0, 8));
        newPayment.setCreatedAt(Instant.now());

        Payment savedPayment = paymentRepository.save(newPayment);

        // trigger the mock payment processor (async - takes ~3 seconds)
        mockPaymentClient.simulatePayment(request.getOrderId(), request.getAmount(), savedPayment.getPaymentId());

        return savedPayment;
    }

    /**
     * Called by payment webhook to update payment status
     * This is where the magic happens after payment gateway responds
     */
    public void processWebhook(String orderId, String status, String paymentId) {
        Payment paymentToUpdate = paymentRepository.findByOrderId(orderId);
        
        if (paymentToUpdate != null) {
            paymentToUpdate.setStatus(status);
            if (paymentId != null) {
                paymentToUpdate.setPaymentId(paymentId);
            }
            paymentRepository.save(paymentToUpdate);
        }

        // also update the order status based on payment result
        Order relatedOrder = orderRepository.findById(orderId).orElse(null);
        if (relatedOrder != null) {
            if ("SUCCESS".equals(status)) {
                relatedOrder.setStatus("PAID");  // yay!
            } else if ("FAILED".equals(status)) {
                relatedOrder.setStatus("FAILED");  // oops
                // TODO: maybe restore inventory here? need to think about this
            }
            orderRepository.save(relatedOrder);
        }
    }

    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}
