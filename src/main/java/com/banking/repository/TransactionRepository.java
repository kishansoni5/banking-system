package com.banking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.TransactionType;

@Repository
public interface TransactionRepository
extends JpaRepository<Transaction, Long> {
	Page<Transaction> findByAccount(Account account,Pageable pagebal);
	Page<Transaction> findByAccountAndType(
	        Account account,
	        TransactionType type,
	        Pageable pageable);
	Page<Transaction> findByAccountAndTimestampBetween(
	        Account account,
	        LocalDateTime start,
	        LocalDateTime end,
	        Pageable pageable);
}
