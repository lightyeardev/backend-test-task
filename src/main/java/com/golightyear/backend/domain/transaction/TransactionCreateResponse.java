package com.golightyear.backend.domain.transaction;

import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Value
public class TransactionCreateResponse {

    @NonNull
    UUID transactionId;

    @NonNull
    UUID accountId;

    @NonNull
    BigDecimal amount;

    @NonNull
    String currency;

    @NonNull
    Instant createTime;

    public static TransactionCreateResponse from(Transaction transaction) {
        return new TransactionCreateResponse(
                transaction.id().value(),
                transaction.accountId().value(),
                transaction.money.value(),
                transaction.money.currency().name(),
                transaction.createTime()
        );
    }

}
