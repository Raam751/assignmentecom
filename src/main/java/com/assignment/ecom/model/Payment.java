package com.assignment.ecom.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.Instant;

@Data
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private String orderId;
    private Double amount;
    private String status;
    private String paymentId;
    private Instant createdAt;
}
