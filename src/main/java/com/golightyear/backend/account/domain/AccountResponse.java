package com.golightyear.backend.account.domain;

import lombok.Value;

import java.time.Instant;

@Value
public class AccountResponse {

    AccountId id;
    AccountName name;
    AccountBalance balance;
    AccountState state;
    Instant createTime;
    Instant lastModified;

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.id(),
                account.name(),
                account.balance(),
                account.state(),
                account.createTime(),
                account.lastModified()
        );
    }
}

