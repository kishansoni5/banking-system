package com.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Records what happened — deposit or withdrawal
// Also a plain POJO — no Spring annotation
public class Transaction {

    private String accountId;         // which account this belongs to
    private String type;              // "DEPOSIT" or "WITHDRAWAL"
    private BigDecimal amount;        // how much
    private LocalDateTime timestamp;  // when it happened

    public Transaction(String accountId, String type, BigDecimal amount) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now(); // auto-set at creation time
    }

    // Getters
    public String getAccountId() { return accountId; }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format(
            "Transaction[account=%s, type=%s, amount=%.2f, time=%s]",
            accountId, type, amount, timestamp
        );
    }
}