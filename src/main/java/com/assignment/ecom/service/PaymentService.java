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
        Order order = orderRepository.findById(request.getOrderId()).orElse(null);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Order is not in CREATED status");
        }

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setStatus("PENDING");
        payment.setPaymentId("pay_" + UUID.randomUUID().toString().substring(0, 8));
        payment.setCreatedAt(Instant.now());

        Payment savedPayment = paymentRepository.save(payment);

        mockPaymentClient.simulatePayment(request.getOrderId(), request.getAmount(), savedPayment.getPaymentId());

        return savedPayment;
    }

    public void processWebhook(String orderId, String status, String paymentId) {
        Payment payment = paymentRepository.findByOrderId(orderId);
        if (payment != null) {
            payment.setStatus(status);
            if (paymentId != null) {
                payment.setPaymentId(paymentId);
            }
            paymentRepository.save(payment);
        }

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            if ("SUCCESS".equals(status)) {
                order.setStatus("PAID");
            } else if ("FAILED".equals(status)) {
                order.setStatus("FAILED");
            }
            orderRepository.save(order);
        }
    }

    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}
