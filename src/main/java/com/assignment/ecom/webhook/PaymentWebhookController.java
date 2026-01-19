package com.assignment.ecom.webhook;

import com.assignment.ecom.service.PaymentService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class PaymentWebhookController {
    private final PaymentService paymentService;

    @PostMapping("/payment")
    public Map<String, String> handlePaymentWebhook(@RequestBody Map<String, Object> payload) {
        String orderId = (String) payload.get("orderId");
        String status = (String) payload.get("status");
        String paymentId = (String) payload.get("paymentId");

        if (orderId != null && status != null) {
            paymentService.processWebhook(orderId, status, paymentId);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Webhook processed successfully");
        return response;
    }
}
