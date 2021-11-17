package com.golightyear.backend.infrastructure.persistence.transaction;

import com.golightyear.backend.account.domain.transaction.Transaction;
import com.golightyear.backend.account.domain.transaction.TransactionRepository;
import com.golightyear.backend.infrastructure.persistence.ApplicationJdbcOperations;

public class TransactionRepositoryJdbc implements TransactionRepository {
    private final AddTransaction addTransaction;

    public TransactionRepositoryJdbc(ApplicationJdbcOperations jdbcOperations) {
        this.addTransaction = new AddTransaction(jdbcOperations);
    }

    @Override
    public void add(Transaction transaction) {
        addTransaction.execute(transaction);
    }
}
