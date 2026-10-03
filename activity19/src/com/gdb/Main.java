package com.gdb;

import com.gdb.command.TransactionCommand;
import com.gdb.domain.IAccount;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InvalidPinException;
import com.gdb.logging.FileLogDestination;
import com.gdb.logging.LogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Global Digital Bank — Service Demo");
        System.out.println("=".repeat(60));

        // ============================================================
        // 📝 STEP 20: Wire Up Dependencies
        //
        // INSTRUCTIONS:
        //   1. LogDestination dest = new FileLogDestination();
        //   2. TransactionLogger logger = new TransactionLogger(dest);
        //   3. AccountService service = new AccountService(logger);
        // ============================================================
        LogDestination destination = new FileLogDestination();
        TransactionLogger logger = new TransactionLogger(destination);
        AccountService service = new AccountService(logger);

        // ============================================================
        // 📝 STEP 21: Run A Demo Workflow
        //
        // INSTRUCTIONS:
        //   1. Open accounts, set PINs, perform deposit, withdraw, transfer.
        //   2. Print balances and transaction history.
        // ============================================================
        IAccount john = service.openAccount("SAVINGS", "John Doe", 25, 15000);
        john.setPin(1234);
        IAccount jane = service.openAccount("SAVINGS", "Jane Smith", 30, 10000);
        jane.setPin(5678);
        System.out.println("[STEP 13] Opened: " + john.getAccountInfo());
        System.out.println("[STEP 13] Opened: " + jane.getAccountInfo());

        Transaction txn = service.deposit(john.getAccountNumber(), 5000);
        System.out.println("\n[STEP 14] Deposit: " + txn);

        Transaction wtxn = service.withdraw(john.getAccountNumber(), 2000, 1234);
        System.out.println("[STEP 15] Withdrawal: " + wtxn);

        Transaction ttxn = service.transfer(john.getAccountNumber(), jane.getAccountNumber(), 1000, 1234);
        System.out.println("[STEP 16] Transfer: " + ttxn);

        System.out.println("\n[STEP 17] Final Balances:");
        System.out.println("  John (Account #" + john.getAccountNumber() + "): Rs. " + john.getBalance());
        System.out.println("  Jane (Account #" + jane.getAccountNumber() + "): Rs. " + jane.getBalance());

        List<TransactionCommand> history = service.getTransactionHistory();
        System.out.println("\n[STEP 18] Transaction History (" + history.size() + " records):");
        for (int i = 0; i < history.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + history.get(i).getTransaction());
        }

        System.out.println("\n[STEP 19] Error Handling Checks:");
        try {
            service.deposit(9999, 1000);
        } catch (AccountException e) {
            System.out.println("  Deposit to missing account caught: " + e.getMessage() + " [PASS]");
        }
        try {
            service.withdraw(john.getAccountNumber(), 500, 9999);
        } catch (InvalidPinException e) {
            System.out.println("  Withdrawal with wrong PIN caught: " + e.getMessage() + " [PASS]");
        }
    }
}
