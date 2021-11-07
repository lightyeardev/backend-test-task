package com.golightyear.backend.infrastructure.persistence.transaction;

import com.golightyear.backend.domain.account.AccountId;
import com.golightyear.backend.domain.transaction.Transaction;
import com.golightyear.backend.domain.transaction.TransactionId;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    void save(Transaction transaction);

    void saveAll(List<Transaction> transactions);

    Optional<Transaction> findById(TransactionId transactionId);

    List<Transaction> findByAccountId(AccountId accountId);

}
