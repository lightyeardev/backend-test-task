package com.golightyear.backend.account.domain.balance;

import com.golightyear.backend.account.domain.account.AccountId;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
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

    public Balance credit(BigDecimal amount) {
        return Balance.builder()
            .id(this.id)
            .accountId(this.accountId)
            .version(this.version + 1)
            .amount(this.amount.add(amount))
            .createTime(this.createTime)
            .build();
    }

    public Balance debit(BigDecimal amount) {
        return Balance.builder()
            .id(this.id)
            .accountId(this.accountId)
            .version(this.version + 1)
            .amount(this.amount.subtract(amount))
            .createTime(this.createTime)
            .build();
    }
}
