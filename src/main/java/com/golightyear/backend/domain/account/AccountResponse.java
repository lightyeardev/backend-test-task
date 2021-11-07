package com.golightyear.backend.domain.account;

import lombok.Value;

import java.time.Instant;

@Value
public class AccountResponse {

    AccountId id;
    AccountName name;
    AccountState state;
    Instant createTime;
    Instant lastModified;

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.id(),
                account.name(),
                account.state(),
                account.createTime(),
                account.lastModified()
        );
    }
}

