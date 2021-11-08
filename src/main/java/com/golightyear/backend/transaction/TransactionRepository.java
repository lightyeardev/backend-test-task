package com.golightyear.backend.transaction;

import com.golightyear.backend.transaction.domain.Transaction;

public interface TransactionRepository {

    void add(Transaction transaction);

}
