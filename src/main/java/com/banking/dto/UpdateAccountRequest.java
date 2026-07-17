package com.banking.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAccountRequest(@NotBlank String holderName) {

}
