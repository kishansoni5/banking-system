package com.banking.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record TransferRequest(
		@NotBlank
		String sourceAccountId,
		@NotBlank
        String targetAccountId,
        @Positive
        BigDecimal amount) {

}
