package com.banking.strategy;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

// [CONCEPT: @Primary]
// When service asks for PaymentStrategy without @Qualifier
// Spring picks THIS bean by default
// Think of it as the "default payment method" on your phone
//
// [CONCEPT: @Component]
// Not @Service or @Repository — those have specific semantic meaning
// Strategy classes are generic components — @Component is correct here

@Component
@Primary
public class UpiPayment implements PaymentStrategy {

    @Override
    public void processPayment(String accountId, BigDecimal amount) {
        System.out.println("💳 Processing UPI Payment...");
        System.out.println("   Method      : UPI");
        System.out.println("   Account ID  : " + accountId);
        System.out.println("   Amount      : ₹" + amount);
        System.out.println("   Status      : SUCCESS via UPI");
    }

    @Override
    public String getPaymentType() {
        return "UPI";
    }
}