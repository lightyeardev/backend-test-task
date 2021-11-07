package com.golightyear.backend.application.transaction;

import com.golightyear.backend.domain.account.AccountId;
import com.golightyear.backend.domain.common.InvalidTransactionException;
import com.golightyear.backend.domain.transaction.CreditTransaction;
import com.golightyear.backend.domain.transaction.DebitTransaction;
import com.golightyear.backend.domain.transaction.Money;
import com.golightyear.backend.domain.transaction.Transaction;
import com.golightyear.backend.domain.transaction.TransactionId;
import com.golightyear.backend.domain.transaction.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionFactory {

    public Transaction create(TransactionId id, TransactionType type, AccountId accountId, Money money, Instant createTime) {
        var transaction = transaction(id, type, accountId, money, createTime);
        transaction.created();
        return transaction;
    }

    public Transaction existing(TransactionId id, TransactionType type, AccountId accountId, Money money, Instant createTime) {
        return transaction(id, type, accountId, money, createTime);
    }

    private Transaction transaction(TransactionId id, TransactionType type, AccountId accountId, Money money, Instant createTime) {
        if (type == TransactionType.CREDIT) {
            return creditTransaction(id, accountId, money, createTime);
        } else if (type == TransactionType.DEBIT) {
            return debitTransaction(id, accountId, money, createTime);
        }
        throw new InvalidTransactionException(String.format("Transaction type %s does not exist", type));
    }

    private Transaction creditTransaction(TransactionId id, AccountId accountId, Money money, Instant createTime) {
        return new CreditTransaction(id, accountId, money, createTime);
    }

    private Transaction debitTransaction(TransactionId id, AccountId accountId, Money money, Instant createTime) {
        return new DebitTransaction(id, accountId, money, createTime);
    }
}
