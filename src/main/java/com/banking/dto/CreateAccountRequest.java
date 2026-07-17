package com.banking.dto;

import com.banking.model.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(
	    @NotBlank(message = "Holder name is required")
        String holderName,
        @NotNull(message = "Account type is required")
        AccountType accountType
) {
}