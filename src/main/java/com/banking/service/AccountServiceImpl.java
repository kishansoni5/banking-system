package com.banking.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.banking.exception.AccountNotFoundException;
import com.banking.exception.InsufficientBalanceException;
import com.banking.exception.InvalidAmountException;
import com.banking.model.Account;
import com.banking.model.AccountType;
import com.banking.model.Transaction;
import com.banking.model.TransactionType;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import com.banking.strategy.PaymentStrategy;

import jakarta.transaction.Transactional;


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
    private final TransactionRepository transactionRepository;

    // [CONCEPT: @Qualifier]
    // We have 3 PaymentStrategy implementations — UPI, Card, Cash
    // Spring gets confused — which one do I inject?
    // @Qualifier("upiPayment") tells Spring exactly which bean to pick
    // Bean name defaults to class name with lowercase first letter
    public AccountServiceImpl(AccountRepository accountRepository,
                               @Qualifier("upiPayment") PaymentStrategy paymentStrategy,TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.paymentStrategy = paymentStrategy;
        this.transactionRepository =transactionRepository;
    }

    public Account getAccountOrThrow(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found: " + id));
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
        Account account = getAccountOrThrow(accountId);

        // Apply deposit — update balance
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);

        // Persist updated account
        accountRepository.save(account);
        
        Transaction transaction=new Transaction(account,TransactionType.DEPOSIT,amount);
        transactionRepository.save(transaction);
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

        Account account = getAccountOrThrow(accountId);

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
        
        Transaction transaction=new Transaction(account,TransactionType.WITHDRAWAL,amount);
        transactionRepository.save(transaction);

        System.out.println("✅ Amount withdrawn successfully!");
        System.out.println("   Account ID  : " + accountId);
        System.out.println("   Withdrawn   : " + amount);
        System.out.println("   New Balance : " + newBalance);
    }

    @Override
    public BigDecimal checkBalance(String accountId) {

        Account account = getAccountOrThrow(accountId);
        System.out.println("✅ Balance retrieved!");
        System.out.println("   Account ID  : " + accountId);
        System.out.println("   Holder Name : " + account.getHolderName());
        System.out.println("   Balance     : " + account.getBalance());

        return account.getBalance();
    }

    @Override
    public Page<Account> getAllAccounts(Pageable pageable) {
        Page<Account> accounts = accountRepository.findAll(pageable);
        System.out.println("✅ Total accounts on page: " + accounts.getContent().size());
        System.out.println("✅ Total accounts: " + accounts.getTotalElements());
        return accounts;
    }

    @Override
    @Transactional
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
        Account sourceAccount = getAccountOrThrow(sourceAccountId);

        // Find target account
        Account targetAccount =getAccountOrThrow(targetAccountId);

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
        
        Transaction transaction1=new Transaction(sourceAccount,TransactionType.TRANSFER_OUT,amount);
        transactionRepository.save(transaction1);
        Transaction transaction2=new Transaction(targetAccount,TransactionType.TRANSFER_IN,amount);
        transactionRepository.save(transaction2);

        System.out.println("✅ Transfer successful!");
        System.out.println("   From Account : " + sourceAccountId);
        System.out.println("   To Account   : " + targetAccountId);
        System.out.println("   Amount       : " + amount);
        System.out.println("   From Balance : " + sourceAccount.getBalance());
        System.out.println("   To Balance   : " + targetAccount.getBalance());
    }
    
    @Override
    public void deleteAccount(String accountId) {

        
    	Account account = getAccountOrThrow(accountId);
    	accountRepository.delete(account);

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

        Account account = getAccountOrThrow(accountId);

        account.setHolderName(holderName);

        accountRepository.save(account);

        return account;
    }
    
    @Override
    public Page<Transaction> getTransactions(
            String accountId,
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable) {

        Account account = getAccountOrThrow(accountId);

        // Filter by transaction type
        if (type != null) {
            return transactionRepository.findByAccountAndType(
                    account,
                    type,
                    pageable);
        }

        // Filter by date range
        if (startDate != null && endDate != null) {

            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = endDate.atTime(LocalTime.MAX);
            System.out.println("Date filter is being used");

            return transactionRepository.findByAccountAndTimestampBetween(
                    account,
                    start,
                    end,
                    pageable);
        }

        // No filter → return all transactions
        return transactionRepository.findByAccount(
                account,
                pageable);
    }
 
}