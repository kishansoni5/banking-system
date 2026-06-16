package com.banking.runner;

import com.banking.model.Account;
import com.banking.model.AccountType;
import com.banking.service.AccountService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

// [CONCEPT: @Component]
// Runner is not a Service or Repository
// It is just an entry-point component — @Component is correct
// Spring manages it as a bean so we can get it from context

@Component
public class BankAppRunner {

    // [CONCEPT: Constructor Injection]
    // Runner depends on AccountService INTERFACE — not impl
    // Spring sees this constructor, finds AccountServiceImpl bean
    // and injects it automatically — we never write new AccountServiceImpl()
    private final AccountService accountService;

    public BankAppRunner(AccountService accountService) {
        this.accountService = accountService;
    }

    // Called from Main after context is ready
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     WELCOME TO SMART BANKING SYSTEM  ║");
        System.out.println("╚══════════════════════════════════════╝");

        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {

                case 1 : {
                    // CREATE ACCOUNT
                    System.out.print("Enter holder name: ");
                    String name = scanner.nextLine();

                    System.out.println("Account type: 1. SAVINGS  2. CURRENT");
                    System.out.print("Enter choice: ");
                    int typeChoice = scanner.nextInt();
                    scanner.nextLine();

                    AccountType type = (typeChoice == 1)
                        ? AccountType.SAVINGS
                        : AccountType.CURRENT;

                    accountService.createAccount(name, type);
                }

                case 2 :  {
                    // DEPOSIT
                    System.out.print("Enter account ID: ");
                    String accId = scanner.nextLine();

                    System.out.print("Enter deposit amount: ");
                    BigDecimal amount = scanner.nextBigDecimal();
                    scanner.nextLine();

                    accountService.deposit(accId, amount);
                }

                case 3 :  {
                    // WITHDRAW
                    System.out.print("Enter account ID: ");
                    String accId = scanner.nextLine();

                    System.out.print("Enter withdrawal amount: ");
                    BigDecimal amount = scanner.nextBigDecimal();
                    scanner.nextLine();

                    accountService.withdraw(accId, amount);
                }

                case 4 : {
                    // CHECK BALANCE
                    System.out.print("Enter account ID: ");
                    String accId = scanner.nextLine();
                    accountService.checkBalance(accId);
                }

                case 5 : {
                    // ALL ACCOUNTS
                    System.out.println("\n📋 All Accounts:");
                    List<Account> accounts = accountService.getAllAccounts();
                    if (accounts.isEmpty()) {
                        System.out.println("   No accounts found.");
                    }
                }

                case 0 : {
                    System.out.println("👋 Thank you for using Smart Banking!");
                    running = false;
                }

                default : System.out.println("❌ Invalid choice. Try again.");
            }

            System.out.println(); // spacing between operations
        }

        scanner.close();
    }

    private void printMenu() {
        System.out.println("┌──────────────────────────────────────┐");
        System.out.println("│           BANKING MENU               │");
        System.out.println("├──────────────────────────────────────┤");
        System.out.println("│  1. Create Account                   │");
        System.out.println("│  2. Deposit Money                    │");
        System.out.println("│  3. Withdraw Money                   │");
        System.out.println("│  4. Check Balance                    │");
        System.out.println("│  5. View All Accounts                │");
        System.out.println("│  0. Exit                             │");
        System.out.println("└──────────────────────────────────────┘");
    }
}