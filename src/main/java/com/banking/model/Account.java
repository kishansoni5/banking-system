package com.banking.model;

import java.math.BigDecimal;

// No Spring annotation here — this is a plain POJO
// Spring does NOT need to manage this object
// It is just data — created and passed around by our service layer
public class Account {

    private String id;           // "ACC001" format — readable and flexible
    private String holderName;   // account owner's name
    private BigDecimal balance;  // BigDecimal — NEVER use double for money
                                 // 0.1 + 0.2 = 0.30000000000000004 in double
                                 // BigDecimal gives exact precision
    private AccountType accountType; // SAVINGS or CURRENT — modeled as enum

    // Constructor — used by service layer to create new accounts
    public Account(String id, String holderName, 
                   BigDecimal balance, AccountType accountType) {
        this.id = id;
        this.holderName = holderName;
        this.balance = balance;
        this.accountType = accountType;
    }

    // Getters
    public String getId() { return id; }
    public String getHolderName() { return holderName; }
    public BigDecimal getBalance() { return balance; }
    public AccountType getAccountType() { return accountType; }

    // Setter only for balance — only field that changes after creation
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public void setHolderName(String holderName) {this.holderName=holderName;}

    @Override
    public String toString() {
        return String.format(
            "Account[id=%s, holder=%s, balance=%.2f, type=%s]",
            id, holderName, balance, accountType
        );
    }
}