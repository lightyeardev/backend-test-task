package com.golightyear.backend.transaction.domain;

import com.golightyear.backend.account.domain.AccountId;
import lombok.Value;

import java.time.Instant;

@Value
public class TransactionResponse {

    TransactionId id;
    AccountId fromAccount;
    AccountId toAccount;
    TransactionAmount amount;
    Instant createTime;

    public static TransactionResponse from(Transaction account) {
        return new TransactionResponse(
                account.id(),
                account.fromAccount(),
                account.toAccount(),
                account.amount(),
                account.createTime()
        );
    }
}

