package com.banking.exception;

public class AccountNotFoundException extends RuntimeException {
	
	public AccountNotFoundException(String accoundID) {
		super("❌ Account not found: "+accoundID);
	}
	
}
