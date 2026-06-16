package com.banking.repository;

import com.banking.model.Account;
import org.springframework.stereotype.Repository;

import java.util.*;

// [CONCEPT: @Repository]
// Tells Spring: "manage this class as a bean"
// Spring creates ONE instance (Singleton scope by default)
// and keeps it in the IoC container
//
// @Repository also has a special bonus — Spring automatically
// translates data-access exceptions into Spring's unified
// DataAccessException hierarchy (useful when you add DB later)

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    // [CONCEPT: In-memory storage]
    // No database — just a HashMap
    // Key = account ID, Value = Account object
    // This HashMap lives inside the Spring-managed bean
    // Since bean is Singleton — this map persists for entire app lifetime
    private final Map<String, Account> accountStore = new HashMap<>();

    @Override
    public void save(Account account) {
        accountStore.put(account.getId(), account);
        System.out.println("[Repository] Account saved: " + account.getId());
    }

    @Override
    public Optional<Account> findById(String id) {
        // Optional.ofNullable — returns empty Optional if null
        // instead of throwing NullPointerException
        return Optional.ofNullable(accountStore.get(id));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accountStore.values());
    }

    @Override
    public boolean existsById(String id) {
        return accountStore.containsKey(id);
    }

    @Override
    public void deleteById(String id) {
        accountStore.remove(id);
        System.out.println("[Repository] Account deleted: " + id);
    }
}