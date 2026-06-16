package com.banking.dto;

import com.banking.model.AccountType;

public record CreateAccountRequest(
        String holderName,
        AccountType accountType
) {
}