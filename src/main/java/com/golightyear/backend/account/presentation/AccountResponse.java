package com.golightyear.backend.account.presentation;

import com.golightyear.backend.account.domain.*;
import lombok.Value;

import java.time.Instant;

@Value
public class AccountResponse {

    AccountId               id;
    AccountName             name;
    Money                   balance;
    AccountState            state;
    Instant                 createTime;
    Instant                 lastModified;
    AccountMovementResponse movements;

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.id(),
                account.name(),
                account.calculateBalance(),
                account.state(),
                account.createTime(),
                account.lastModified(),
                AccountMovementResponse.from(account.accountActivity())
        );
    }
}

