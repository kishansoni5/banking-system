package com.banking.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.banking.dto.LoginRequest;
import com.banking.dto.LoginResponse;
import com.banking.security.CustomUserDetails;
import com.banking.security.JwtService;

@Service
public class AuthServiceImpl implements AuthService{

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	public AuthServiceImpl(AuthenticationManager authenticationManager,JwtService jwtService) {
		this.authenticationManager=authenticationManager;
		this.jwtService=jwtService;
	}
	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(loginRequest.username(),loginRequest.password());
		Authentication authentication=authenticationManager.authenticate(token);
		CustomUserDetails userDetails=(CustomUserDetails) authentication.getPrincipal();
		return new LoginResponse(jwtService.generateToken(userDetails),userDetails.getUsername(),userDetails.getUser().getRole());
	}	
}
