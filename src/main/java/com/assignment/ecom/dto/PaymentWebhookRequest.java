package com.assignment.ecom.dto;

import lombok.Data;
import java.util.Map;

@Data
public class PaymentWebhookRequest {
    private String event;
    private Map<String, Object> payload;
}
