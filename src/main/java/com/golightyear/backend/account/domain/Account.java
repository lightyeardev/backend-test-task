package com.golightyear.backend.account.domain;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

import static com.golightyear.backend.account.domain.AccountState.ACTIVE;

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

}
