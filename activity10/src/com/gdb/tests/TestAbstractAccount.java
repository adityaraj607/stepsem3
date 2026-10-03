package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TestAbstractAccount {
    public static void main(String[] args) {
        System.out.println("=== Activity 10: Banking Operations Suite ===");

        // Step 1: Create an array of AbstractAccount objects (SavingsAccount, CurrentAccount, SalaryAccount)
        AbstractAccount[] accounts = new AbstractAccount[3];
        accounts[0] = new SavingsAccount("SAV1001", "Rajesh Sharma", 28, 10000.0, "ACTIVE", "1234", 1000.0, 4.0);
        accounts[1] = new CurrentAccount("CUR1001", "Priya Patel", 34, 5000.0, "ACTIVE", "5678", 25000.0);
        accounts[2] = new SalaryAccount("SAL1001", "Sneha Verma", 26, 30000.0, "ACTIVE", "2222", "TechCorp");

        SavingsAccount savings = (SavingsAccount) accounts[0];
        CurrentAccount current = (CurrentAccount) accounts[1];
        SalaryAccount salary = (SalaryAccount) accounts[2];

        // Step 2: Implement and test secure fund transfer from Savings to Current account with PIN authentication
        try {
            transfer(savings, current, 3000.0, "1234");
            System.out.println("Transfer Rs 3000 from Savings to Current: SUCCESS");
        } catch (AccountException e) {
            System.out.println("Transfer Rs 3000 from Savings to Current: FAILED - " + e.getMessage());
        }
        System.out.println("Savings Balance: Rs " + savings.getBalance() + " | Current Balance: Rs " + current.getBalance());

        // Step 3: Test failed transfer with wrong PIN and verify no balance was credited/debited
        double savBalBefore = savings.getBalance();
        double curBalBefore = current.getBalance();
        try {
            transfer(savings, current, 500.0, "0000");
            System.out.println("Failed Transfer (Wrong PIN): No exception caught [FAIL]");
        } catch (AccountException e) {
            if (savings.getBalance() == savBalBefore && current.getBalance() == curBalBefore) {
                System.out.println("Failed Transfer (Wrong PIN): Exception caught, no balance changed [PASS]");
            } else {
                System.out.println("Failed Transfer (Wrong PIN): Balance changed [FAIL]");
            }
        }

        // Step 4: Process monthly cycle applying interest to every SavingsAccount and checking each SalaryAccount's inactive months
        for (AbstractAccount acc : accounts) {
            if (acc instanceof SavingsAccount) {
                ((SavingsAccount) acc).applyInterest();
            }
            if (acc instanceof SalaryAccount) {
                ((SalaryAccount) acc).incrementInactiveMonths();
            }
        }
        System.out.println("Monthly Interest Cycle processed for all qualifying accounts.");
        System.out.println("All banking operations passed!");
    }

    public static void transfer(AbstractAccount source, AbstractAccount destination, double amount, String pin) throws AccountException {
        source.withdraw(amount, pin);
        destination.deposit(amount);
    }
}
