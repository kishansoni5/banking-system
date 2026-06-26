package com.banking.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banking.dto.CreateAccountRequest;
import com.banking.dto.DepositRequest;
import com.banking.dto.TransferRequest;
import com.banking.dto.UpdateAccountRequest;
import com.banking.dto.WithdrawRequest;
import com.banking.model.Account;
import com.banking.service.AccountService;

/**
 * REST Controller responsible for exposing banking operations
 * through HTTP endpoints.
 *
 * Base URL:
 *      /accounts
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    // Constructor Injection (recommended by Spring)
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // =====================================================
    // CREATE OPERATIONS
    // =====================================================

    /**
     * Create a new bank account.
     *
     * POST /accounts
     */
    @PostMapping
    public ResponseEntity<Account> createAccount(
            @RequestBody CreateAccountRequest request) {

        Account account = accountService.createAccount(
                request.holderName(),
                request.accountType());

        return ResponseEntity.status(201)
                             .body(account);
    }

    // =====================================================
    // READ OPERATIONS
    // =====================================================

    /**
     * Get all accounts.
     *
     * GET /accounts
     */
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {

        List<Account> accounts = accountService.getAllAccounts();

        return ResponseEntity.ok(accounts);
    }
    
    /**
     * Get account by id.
     *
     * GET /accounts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable String id) {

        Account account = accountService.getAccountById(id);

        return ResponseEntity.ok(account);
    }

    /**
     * Check balance for a specific account.
     *
     * GET /accounts/{accountId}/balance
     */
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BigDecimal> getBalance(
            @PathVariable String accountId) {

        BigDecimal balance =
                accountService.checkBalance(accountId);

        return ResponseEntity.ok(balance);
    }

    // =====================================================
    // BUSINESS OPERATIONS
    // =====================================================

    /**
     * Deposit money into an account.
     *
     * POST /accounts/{accountId}/deposit
     */
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<String> deposit(
            @PathVariable String accountId,
            @RequestBody DepositRequest request) {

        accountService.deposit(
                accountId,
                request.amount());

        return ResponseEntity.ok(
                "Deposit successful");
    }

    /**
     * Withdraw money from an account.
     *
     * POST /accounts/{accountId}/withdraw
     */
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<String> withdraw(
            @PathVariable String accountId,
            @RequestBody WithdrawRequest request) {

        accountService.withdraw(
                accountId,
                request.amount());

        return ResponseEntity.ok(
                "Withdrawal successful");
    }

    /**
     * Transfer money between two accounts.
     *
     * POST /accounts/transfer
     */
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
            @RequestBody TransferRequest request) {

        accountService.transfer(
                request.sourceAccountId(),
                request.targetAccountId(),
                request.amount());

        return ResponseEntity.ok(
                "Transfer completed successfully");
    }

    // =====================================================
    // UPDATE OPERATIONS
    // =====================================================

    /**
     * Update account details.
     *
     * PUT /accounts/{accountId}
     */
    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable String accountId,
            @RequestBody UpdateAccountRequest request) {

        Account updatedAccount =
                accountService.updateAccount(
                        accountId,
                        request.holderName());

        return ResponseEntity.ok(updatedAccount);
    }

    // =====================================================
    // DELETE OPERATIONS
    // =====================================================

    /**
     * Delete an account.
     *
     * DELETE /accounts/{accountId}
     */
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable String accountId) {

        accountService.deleteAccount(accountId);

        return ResponseEntity.noContent()
                             .build();
    }
}