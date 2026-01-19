package com.assignment.ecom.client;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MockPaymentClient {
    private final RestTemplate restTemplate;

    @Async
    public void simulatePayment(String orderId, Double amount, String paymentId) {
        try {
            Thread.sleep(3000);
            Map<String, Object> webhookPayload = new HashMap<>();
            webhookPayload.put("orderId", orderId);
            webhookPayload.put("status", "SUCCESS");
            webhookPayload.put("paymentId", paymentId);
            restTemplate.postForObject("http://localhost:8080/api/webhooks/payment", webhookPayload, String.class);
        } catch (Exception e) {
        }
    }
}
