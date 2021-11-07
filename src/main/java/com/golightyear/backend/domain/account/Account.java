package com.golightyear.backend.domain.account;

import com.golightyear.backend.domain.balance.Balance;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.golightyear.backend.domain.account.AccountState.ACTIVE;
import static com.golightyear.backend.domain.account.AccountState.INACTIVE;

@Value
@Builder(toBuilder = true)
public class Account {

    @Builder.Default
    AccountId id = AccountId.random();

    @Builder.Default
    int version = 0;

    AccountName name;

    @Builder.Default
    AccountState state = ACTIVE;

    @Builder.Default
    Instant createTime = Instant.now();

    @Builder.Default
    Instant lastModified = Instant.now();

    @Builder.Default
    List<Balance> balances = new ArrayList<>();

    public boolean isInactive() {
        return this.state == INACTIVE;
    }

}
