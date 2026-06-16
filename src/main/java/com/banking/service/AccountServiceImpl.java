package com.banking.service;

import com.banking.exception.AccountNotFoundException;
import com.banking.exception.InsufficientBalanceException;
import com.banking.exception.InvalidAmountException;
import com.banking.model.Account;
import com.banking.model.AccountType;
import com.banking.repository.AccountRepository;
import com.banking.strategy.PaymentStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

// [CONCEPT: @Service]
// Tells Spring: "this is a business logic bean"
// Functionally same as @Component but semantically meaningful
// Spring creates ONE singleton instance and manages it

@Service
public class AccountServiceImpl implements AccountService {

    // [CONCEPT: Constructor Injection]
    // We are NOT doing this:
    //     @Autowired
    //     private AccountRepository accountRepository;  ← field injection (bad)
    //
    // We ARE doing this — constructor injection (best practice)
    // Why? Because:
    // 1. Dependencies are explicit and visible
    // 2. Class can be unit tested without Spring context
    // 3. Dependencies are immutable (final keyword)
    // 4. Spring team itself recommends constructor injection

    private final AccountRepository accountRepository;
    private final PaymentStrategy paymentStrategy;

    // [CONCEPT: @Qualifier]
    // We have 3 PaymentStrategy implementations — UPI, Card, Cash
    // Spring gets confused — which one do I inject?
    // @Qualifier("upiPayment") tells Spring exactly which bean to pick
    // Bean name defaults to class name with lowercase first letter
    public AccountServiceImpl(AccountRepository accountRepository,
                               @Qualifier("upiPayment") PaymentStrategy paymentStrategy) {
        this.accountRepository = accountRepository;
        this.paymentStrategy = paymentStrategy;
    }

    @Override
    public Account createAccount(String holderName, AccountType accountType) {

        // Generate unique account ID — real banking style
        String accountId = "ACC" + UUID.randomUUID()
                                       .toString()
                                       .substring(0, 8)
                                       .toUpperCase();

        // Create new Account POJO — we use 'new' here because
        // Account is NOT a Spring bean — it is just data
        Account account = new Account(
            accountId,
            holderName,
            BigDecimal.ZERO,   // always starts with zero balance
            accountType
        );

        // Delegate storage to repository layer
        accountRepository.save(account);

        System.out.println("✅ Account created successfully!");
        System.out.println("   Account ID  : " + accountId);
        System.out.println("   Holder Name : " + holderName);
        System.out.println("   Account Type: " + accountType);
        System.out.println("   Balance     : 0.00");

        return account;
    }

    @Override
    public void deposit(String accountId, BigDecimal amount) {

        // Business rule 1 — amount must be positive
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(
                "❌ Deposit amount must be greater than zero"
            );
        }

        // Fetch account — Optional forces us to handle "not found"
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));

        // Apply deposit — update balance
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);

        // Persist updated account
        accountRepository.save(account);

        // Process payment via strategy
        // [CONCEPT: Strategy Pattern + DI]
        // We don't know or care if this is UPI/Card/Cash here
        // We just call processPayment() on the interface
        // Spring injected the right implementation for us
        paymentStrategy.processPayment(accountId, amount);

        System.out.println("✅ Amount deposited successfully!");
        System.out.println("   Account ID  : " + accountId);
        System.out.println("   Deposited   : " + amount);
        System.out.println("   New Balance : " + newBalance);
    }

    @Override
    public void withdraw(String accountId, BigDecimal amount) {

        // Business rule 1 — amount must be positive
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(
                "❌ Withdrawal amount must be greater than zero"
            );
        }

        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));

        // Business rule 2 — cannot withdraw more than balance
        if (amount.compareTo(account.getBalance()) > 0) {
            throw new InsufficientBalanceException(
                "❌ Insufficient balance. Available: " + account.getBalance()
            );
        }

        // Apply withdrawal
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);

        accountRepository.save(account);

        System.out.println("✅ Amount withdrawn successfully!");
        System.out.println("   Account ID  : " + accountId);
        System.out.println("   Withdrawn   : " + amount);
        System.out.println("   New Balance : " + newBalance);
    }

    @Override
    public BigDecimal checkBalance(String accountId) {

        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));

        System.out.println("✅ Balance retrieved!");
        System.out.println("   Account ID  : " + accountId);
        System.out.println("   Holder Name : " + account.getHolderName());
        System.out.println("   Balance     : " + account.getBalance());

        return account.getBalance();
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        System.out.println("✅ Total accounts: " + accounts.size());
        accounts.forEach(a -> System.out.println("   → " + a));
        return accounts;
    }

    @Override
    public void transfer(
            String sourceAccountId,
            String targetAccountId,
            BigDecimal amount) {

        // Amount must be positive
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(
                    "Transfer amount must be greater than zero");
        }

        // Source and target cannot be same
        if (sourceAccountId.equals(targetAccountId)) {
            throw new IllegalArgumentException(
                    "Source and target accounts cannot be the same");
        }

        // Find source account
        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(sourceAccountId));

        // Find target account
        Account targetAccount = accountRepository.findById(targetAccountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(targetAccountId));

        // Check sufficient balance
        if (amount.compareTo(sourceAccount.getBalance()) > 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: "
                            + sourceAccount.getBalance());
        }

        // Update balances
        sourceAccount.setBalance(
                sourceAccount.getBalance().subtract(amount));

        targetAccount.setBalance(
                targetAccount.getBalance().add(amount));

        // Save both accounts
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        System.out.println("✅ Transfer successful!");
        System.out.println("   From Account : " + sourceAccountId);
        System.out.println("   To Account   : " + targetAccountId);
        System.out.println("   Amount       : " + amount);
        System.out.println("   From Balance : " + sourceAccount.getBalance());
        System.out.println("   To Balance   : " + targetAccount.getBalance());
    }
    
    @Override
    public void deleteAccount(String accountId) {

        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException(accountId);
        }

        accountRepository.deleteById(accountId);

        System.out.println("✅ Account deleted: " + accountId);
    }
    
    @Override
    public Account updateAccount(
            String accountId,
            String holderName) {

        if (holderName == null || holderName.isBlank()) {
            throw new InvalidAmountException(
                    "Holder name cannot be empty");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(accountId));

        account.setHolderName(holderName);

        accountRepository.save(account);

        return account;
    }
}