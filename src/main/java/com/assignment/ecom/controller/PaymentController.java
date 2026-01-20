package com.assignment.ecom.controller;

import com.assignment.ecom.dto.PaymentRequest;
import com.assignment.ecom.model.Payment;
import com.assignment.ecom.service.PaymentService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create")
    public Payment createPayment(@RequestBody PaymentRequest request) {
        // kicks off payment process - webhook will update status later
        return paymentService.createPayment(request);
    }

    @GetMapping("/order/{orderId}")
    public Payment getPaymentByOrder(@PathVariable String orderId) {
        return paymentService.getPaymentByOrderId(orderId);
    }
}
