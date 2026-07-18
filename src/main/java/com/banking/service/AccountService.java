package com.banking.service;

import com.banking.model.Account;
import com.banking.model.AccountType;
import com.banking.model.Transaction;
import com.banking.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// CONTRACT — defines what banking operations are available
// Controller layer depends on THIS interface, not the implementation
public interface AccountService {

    Account createAccount(String holderName, AccountType accountType);
    void deposit(String accountId, BigDecimal amount);
    void withdraw(String accountId, BigDecimal amount);
    BigDecimal checkBalance(String accountId);
    Page<Account> getAllAccounts(Pageable pageable);
    void transfer(String fromAccountId,String toAccountId,BigDecimal amount);
    void deleteAccount(String accountId);
    Account updateAccount(String accountId,String holderName);
	Account getAccountOrThrow(String id);
	public Page<Transaction> getTransactions(
	        String accountId,
	        TransactionType type,
	        LocalDate startDate,
	        LocalDate endDate,
	        Pageable pageable);
}