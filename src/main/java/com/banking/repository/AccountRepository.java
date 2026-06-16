package com.banking.repository;

import com.banking.model.Account;
import java.util.List;
import java.util.Optional;

// This is the CONTRACT — defines WHAT operations are possible
// No implementation details here — just method signatures
public interface AccountRepository {

    void save(Account account);           // create / update account
    Optional<Account> findById(String id); // find one account — Optional 
                                           // avoids NullPointerException
    List<Account> findAll();              // get all accounts
    boolean existsById(String id);        // check before operations
    void deleteById(String id);           // remove account
}