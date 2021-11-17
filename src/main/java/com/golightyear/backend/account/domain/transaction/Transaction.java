package com.golightyear.backend.account.domain.transaction;

import com.golightyear.backend.account.domain.account.AccountId;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class Transaction {
    @Builder.Default
    TransactionId id = TransactionId.random();

    @Builder.Default
    TransactionType type = TransactionType.CREDIT;

    @Builder.Default
    AccountId accountId = AccountId.random();

    @Builder.Default
    TransactionAmount amount = new TransactionAmount(BigDecimal.ZERO);

    @Builder.Default
    Instant createTime = Instant.now();
}
