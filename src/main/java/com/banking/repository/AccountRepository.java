package com.banking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.banking.model.Account;

public interface AccountRepository
        extends JpaRepository<Account, String> {
	Optional<Account> findByHolderName(String holderName);
}