package com.golightyear.backend.domain.transaction;

import com.golightyear.backend.domain.account.AccountId;

import java.time.Instant;

public class CreditTransaction extends Transaction {

    public CreditTransaction(TransactionId id, AccountId accountId, Money money, Instant createTime) {
        super(id, TransactionType.CREDIT, accountId, money, createTime);
    }
}
