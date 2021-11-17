package com.golightyear.backend.account.presentation;

import com.golightyear.backend.account.domain.account.Account;
import com.golightyear.backend.account.domain.account.AccountId;
import com.golightyear.backend.account.domain.account.AccountName;
import com.golightyear.backend.account.domain.account.AccountState;
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

