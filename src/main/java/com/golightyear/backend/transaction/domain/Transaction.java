package com.golightyear.backend.transaction.domain;

import com.golightyear.backend.account.domain.AccountId;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class Transaction {

    @Builder.Default
    TransactionId id = TransactionId.random();

    AccountId fromAccount;
    AccountId toAccount;
    TransactionAmount amount;

    @Builder.Default
    Instant createTime = Instant.now();

}
