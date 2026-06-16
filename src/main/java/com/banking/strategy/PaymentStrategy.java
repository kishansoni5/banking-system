package com.banking.strategy;

import java.math.BigDecimal;

// CONTRACT — every payment type must implement this
// This is what service layer depends on — not the impl
public interface PaymentStrategy {

    void processPayment(String accountId, BigDecimal amount);
    String getPaymentType(); // identifies which strategy is active
}