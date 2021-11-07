package com.golightyear.backend.domain.transaction;

import com.golightyear.backend.domain.account.AccountId;

import java.time.Instant;

public class DebitTransaction extends Transaction {

    public DebitTransaction(TransactionId id, AccountId accountId, Money money, Instant createTime) {
        super(id, TransactionType.DEBIT, accountId, money, createTime);
    }
}
