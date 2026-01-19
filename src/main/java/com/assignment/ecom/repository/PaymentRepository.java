package com.assignment.ecom.repository;

import com.assignment.ecom.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    Payment findByOrderId(String orderId);

    Payment findByPaymentId(String paymentId);
}
