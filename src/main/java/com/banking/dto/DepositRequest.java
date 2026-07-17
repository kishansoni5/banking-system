package com.banking.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Positive;

public record DepositRequest(
		@Positive
        BigDecimal amount
) {
}