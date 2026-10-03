package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import com.gdb.db.SimulatedDatabase;
import java.util.ArrayList;
import java.util.List;

public class DatabaseLogDestination implements LogDestination {
    private static final String TABLE = "transaction_log";
    private final SimulatedDatabase db;

    public DatabaseLogDestination(SimulatedDatabase db) {
        this.db = db;
    }

    @Override
    public void write(TransactionCommand cmd) {
        db.insert(TABLE, cmd);
    }

    @Override
    public List<TransactionCommand> readAll() {
        List<Object> rows = db.selectAll(TABLE);
        List<TransactionCommand> list = new ArrayList<>();
        for (Object obj : rows) {
            list.add((TransactionCommand) obj);
        }
        return list;
    }

    @Override
    public void clear() {
        db.deleteAll(TABLE);
    }

    @Override
    public String getDestinationName() {
        return "DATABASE";
    }
}
