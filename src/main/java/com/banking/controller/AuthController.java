package com.banking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.dto.LoginRequest;
import com.banking.dto.LoginResponse;
import com.banking.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	public AuthController(AuthService authService) {
		this.authService=authService;
	}
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(
			@Valid @RequestBody LoginRequest loginRequest) {
		 System.out.println("Controller reached");
		LoginResponse loginResponse=authService.login(loginRequest);
		return ResponseEntity.ok(loginResponse);
	}
}
