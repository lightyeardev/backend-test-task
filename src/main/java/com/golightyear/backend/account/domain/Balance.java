package com.golightyear.backend.account.domain;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class Balance {
    @Builder.Default
    BalanceId id = BalanceId.random();

    AccountId accountId;

    @Builder.Default
    int version = 0;

    @Builder.Default
    BalanceAmount amount = BalanceAmount.fresh();

    @Builder.Default
    Instant createTime = Instant.now();

    @Builder.Default
    Instant lastModified = Instant.now();
}
