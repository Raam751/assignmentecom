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

    /**
     * Payment gateway calls this endpoint when payment is processed
     * In real life this would need authentication/signature verification
     */
    @PostMapping("/payment")
    public Map<String, String> handlePaymentWebhook(@RequestBody Map<String, Object> payload) {
        String orderId = (String) payload.get("orderId");
        String status = (String) payload.get("status");
        String paymentId = (String) payload.get("paymentId");

        // basic validation - should probably be more robust
        if (orderId != null && status != null) {
            paymentService.processWebhook(orderId, status, paymentId);
        }

        Map<String, String> webhookResponse = new HashMap<>();
        webhookResponse.put("message", "Webhook processed successfully");
        return webhookResponse;
    }
}
