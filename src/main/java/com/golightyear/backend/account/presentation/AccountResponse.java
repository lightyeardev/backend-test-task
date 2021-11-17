package com.golightyear.backend.account.presentation;

import com.golightyear.backend.account.domain.Account;
import com.golightyear.backend.account.domain.AccountId;
import com.golightyear.backend.account.domain.AccountName;
import com.golightyear.backend.account.domain.AccountState;
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

