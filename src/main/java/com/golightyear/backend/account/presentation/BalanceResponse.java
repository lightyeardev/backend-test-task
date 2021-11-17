package com.golightyear.backend.account.presentation;

import com.golightyear.backend.account.domain.account.AccountId;
import com.golightyear.backend.account.domain.balance.Balance;
import com.golightyear.backend.account.domain.balance.BalanceAmount;
import lombok.Value;

import java.time.Instant;

@Value
public class BalanceResponse {
    AccountId id;
    BalanceAmount amount;
    Instant createTime;
    Instant lastModified;

    public static BalanceResponse from(Balance balance) {
        return new BalanceResponse(
            balance.accountId(),
            balance.amount(),
            balance.createTime(),
            balance.lastModified()
        );
    }
}
