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

    /**
     * Simulates payment gateway processing
     * In real scenario this would call actual payment API like Stripe/PayPal
     * 
     * For demo: waits 3 seconds then auto-approves and calls our webhook
     */
    @Async
    public void simulatePayment(String orderId, Double amount, String paymentId) {
        try {
            // simulate payment processing delay
            Thread.sleep(3000);
            
            // build webhook callback payload
            Map<String, Object> webhookData = new HashMap<>();
            webhookData.put("orderId", orderId);
            webhookData.put("status", "SUCCESS");  // always succeeds for demo
            webhookData.put("paymentId", paymentId);
            
            // call our own webhook endpoint (learned this pattern from stripe docs)
            restTemplate.postForObject("http://localhost:8080/api/webhooks/payment", webhookData, String.class);
            
        } catch (Exception e) {
            // swallow exceptions - probably not ideal but works for demo
            // System.out.println("Payment simulation failed: " + e.getMessage());
        }
    }
}
