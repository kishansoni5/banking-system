package com.banking.strategy;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

// [CONCEPT: @Component without @Primary]
// This bean exists in container but is NOT the default
// Only injected when explicitly requested via
// @Qualifier("cardPayment") somewhere
//
// Bean name = class name with lowercase first letter
// "CardPayment" → bean name = "cardPayment"

@Component
public class CardPayment implements PaymentStrategy {

    @Override
    public void processPayment(String accountId, BigDecimal amount) {
        System.out.println("💳 Processing Card Payment...");
        System.out.println("   Method      : CARD");
        System.out.println("   Account ID  : " + accountId);
        System.out.println("   Amount      : ₹" + amount);
        System.out.println("   Status      : SUCCESS via Card");
    }

    @Override
    public String getPaymentType() {
        return "CARD";
    }
}