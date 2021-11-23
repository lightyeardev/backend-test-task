package com.golightyear.backend.account.domain;

import lombok.*;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class Ledger {

    @Builder.Default
    LedgerId id = LedgerId.random();

    AccountId debitAccountId;

    AccountId creditAccountId;

    Operation operation;

    @NonNull
    Money amount;

    @Builder.Default
    Instant createTime = Instant.now();
}
