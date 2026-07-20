package com.banking.service;

import com.banking.dto.LoginRequest;
import com.banking.dto.LoginResponse;

public interface AuthService {
	LoginResponse login(LoginRequest loginRequest);
}
