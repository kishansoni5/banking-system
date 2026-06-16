package com.banking.strategy;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

// Same as CardPayment — exists in container
// Injected only when @Qualifier("cashPayment") is used

@Component
public class CashPayment implements PaymentStrategy {

    @Override
    public void processPayment(String accountId, BigDecimal amount) {
        System.out.println("💵 Processing Cash Payment...");
        System.out.println("   Method      : CASH");
        System.out.println("   Account ID  : " + accountId);
        System.out.println("   Amount      : ₹" + amount);
        System.out.println("   Status      : SUCCESS via Cash");
    }

    @Override
    public String getPaymentType() {
        return "CASH";
    }
}