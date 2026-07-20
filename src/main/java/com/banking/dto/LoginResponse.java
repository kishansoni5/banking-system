package com.banking.dto;

import com.banking.model.Role;

public record LoginResponse(
		String token,
		String username,
		Role role) {

}
