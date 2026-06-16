package com.banking.model;

// Enum lives in model package alongside Account
// Enums for fixed options — cleaner than String "SAVINGS" / "savings"
public enum AccountType {
    SAVINGS,
    CURRENT
}