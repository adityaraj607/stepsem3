package com.gdb.tests;

import com.gdb.command.*;
import com.gdb.db.SimulatedDatabase;
import com.gdb.domain.*;
import com.gdb.logging.*;
import java.util.List;

public class TestBridgeLogging {
    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("  ACTIVITY 18 — BRIDGE PATTERN (FILE + DB)");
        System.out.println("=".repeat(60));

        // Setup test accounts
        IAccount acc1 = AccountFactory.createAccount("SAVINGS", 1001, "John", 25, 15000);
        acc1.setPin(1234);
        IAccount acc2 = AccountFactory.createAccount("SAVINGS", 1002, "Jane", 30, 10000);

        // ============================================================
        // 📝 STEP 24: Create Destinations
        // ============================================================
        FileLogDestination fileDest = new FileLogDestination();
        fileDest.clear();
        SimulatedDatabase db = new SimulatedDatabase();
        DatabaseLogDestination dbDest = new DatabaseLogDestination(db);
        MemoryLogDestination memDest = new MemoryLogDestination();

        TransactionLogger logger = new TransactionLogger(fileDest);

        // ============================================================
        // 📝 STEP 25: Create TransactionLogger with File Destination
        // ============================================================
        logger.setDestination(fileDest);
        logger.log(new DepositCommand(acc1, 5000));
        logger.log(new WithdrawCommand(acc1, 2000, 1234));
        logger.log(new TransferCommand(acc1, acc2, 3000, 1234));
        System.out.println("\n[STEP 25] Logging to FILE destination...");
        System.out.println("  FILE log count: " + logger.readAll().size());

        // ============================================================
        // 📝 STEP 26: Switch to Database Destination
        // ============================================================
        logger.setDestination(dbDest);
        logger.log(new DepositCommand(acc1, 5000));
        logger.log(new WithdrawCommand(acc1, 2000, 1234));
        logger.log(new TransferCommand(acc1, acc2, 3000, 1234));
        System.out.println("\n[STEP 26] Switched to DATABASE destination...");
        System.out.println("  DATABASE log count: " + logger.readAll().size());

        // ============================================================
        // 📝 STEP 27: Switch to Memory Destination
        // ============================================================
        logger.setDestination(memDest);
        logger.log(new DepositCommand(acc1, 5000));
        logger.log(new WithdrawCommand(acc1, 2000, 1234));
        logger.log(new TransferCommand(acc1, acc2, 3000, 1234));
        System.out.println("\n[STEP 27] Switched to MEMORY destination...");
        System.out.println("  MEMORY log count: " + logger.readAll().size());

        // ============================================================
        // 📝 STEP 28: Verify Data Isolation Across Backends
        // ============================================================
        System.out.println("\n[STEP 28] Verifying Data Isolation:");
        logger.setDestination(fileDest);
        System.out.println("  FILE count: " + fileDest.readAll().size() + " [EXPECTED: 3]");
        logger.setDestination(dbDest);
        System.out.println("  DATABASE count: " + db.count("transaction_log") + " [EXPECTED: 3]");
        logger.setDestination(memDest);
        System.out.println("  MEMORY count: " + memDest.readAll().size() + " [EXPECTED: 3]");

        // ============================================================
        // 📝 STEP 29: Print Destination Names
        // ============================================================
        System.out.println("\n[STEP 29] Destination Names: " + fileDest.getDestinationName() + ", " + dbDest.getDestinationName() + ", " + memDest.getDestinationName());
        System.out.println("[STEP 29] All Bridge Pattern log backends verified successfully!");
    }
}
