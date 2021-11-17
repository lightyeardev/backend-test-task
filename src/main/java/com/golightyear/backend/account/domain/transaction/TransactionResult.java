package com.golightyear.backend.account.domain.transaction;

import com.golightyear.backend.account.domain.account.AccountId;
import com.golightyear.backend.account.domain.balance.BalanceAmount;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class TransactionResult {
    @Builder.Default
    TransactionId id = TransactionId.random();

    @Builder.Default
    AccountId accountId = AccountId.random();

    @Builder.Default
    BalanceAmount balanceAmount = BalanceAmount.fresh();

    @Builder.Default
    Instant createTime = Instant.now();
}
