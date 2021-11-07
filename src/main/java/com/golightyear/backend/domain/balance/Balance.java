package com.golightyear.backend.domain.balance;

import com.golightyear.backend.domain.account.AccountId;
import com.golightyear.backend.domain.transaction.Money;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;

import static com.golightyear.backend.domain.balance.BalanceState.ACTIVE;

@Getter
@Builder(toBuilder = true)
public class Balance {

    @Builder.Default
    BalanceId id = BalanceId.random();

    @Builder.Default
    int version = 0;

    @NonNull
    AccountId accountId;

    @NonNull
    Money money;

    @Builder.Default
    BalanceState state = ACTIVE;

    @Builder.Default
    Instant createTime = Instant.now();

    @Builder.Default
    Instant lastModified = Instant.now();

    public void withdraw(Money money) {
        this.money = this.money.subtract(money);
        this.lastModified = Instant.now();
    }

    public void deposit(Money money) {
        this.money = this.money.add(money);
        this.lastModified = Instant.now();
    }

    public boolean isInactive() {
        return this.state == BalanceState.INACTIVE;
    }

}
