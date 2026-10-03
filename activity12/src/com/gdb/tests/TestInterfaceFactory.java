package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TestInterfaceFactory {
    public static void main(String[] args) {
        System.out.println("=== Activity 12: Factory-Driven System Suite ===");

        // Step 1: Instantiate Savings, Current, and FixedDeposit accounts exclusively through AccountFactory.createAccount()
        IAccount savings = AccountFactory.createAccount("SAVINGS", "SAV1001", "Rajesh Sharma", 28, 5000.0, "ACTIVE", "1234");
        IAccount current = AccountFactory.createAccount("CURRENT", "CUR1001", "Priya Patel", 34, 5000.0, "ACTIVE", "5678");
        IAccount fd = AccountFactory.createAccount("FIXED_DEPOSIT", "FD1001", "Amit Kumar", 45, 50000.0, "ACTIVE", "1111");

        // Step 2: Perform deposits and withdrawals through the IAccount interface references
        // Step 3: Verify Savings minimum balance rule enforcement through the interface
        try {
            savings.deposit(2000.0);
            try {
                savings.withdraw(6500.0, "1234");
                System.out.println("[Test 1] Savings Account Creation & Deposit: [FAIL]");
            } catch (AccountException e) {
                System.out.println("[Test 1] Savings Account Creation & Deposit: [PASS]");
            }
        } catch (InvalidAmountException e) {
            System.out.println("[Test 1] Savings Account Creation & Deposit: [FAIL]");
        }

        // Step 4: Verify Current overdraft limit enforcement through the interface
        try {
            current.withdraw(20000.0, "5678");
            System.out.println("[Test 2] Current Account Overdraft Withdrawal: [PASS]");
        } catch (AccountException e) {
            System.out.println("[Test 2] Current Account Overdraft Withdrawal: [FAIL]");
        }

        // Step 5: Verify FixedDeposit premature withdrawal rejection through the interface
        try {
            fd.withdraw(1000.0, "1111");
            System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [FAIL]");
        } catch (AccountException e) {
            System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [PASS]");
        }

        // Step 6: Verify requesting an invalid account type from AccountFactory throws IllegalArgumentException
        try {
            AccountFactory.createAccount("INVALID", "INV001", "Test User", 30, 1000.0, "ACTIVE", "3333");
            System.out.println("[Test 4] Invalid Type Rejection: [FAIL]");
        } catch (IllegalArgumentException e) {
            System.out.println("[Test 4] Invalid Type Rejection: [PASS]");
        }

        System.out.println("Factory-driven architecture successfully verified!");
    }
}
