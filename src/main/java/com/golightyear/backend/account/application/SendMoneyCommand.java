package com.golightyear.backend.account.application;

import com.golightyear.backend.account.domain.AccountId;
import com.golightyear.backend.account.domain.Money;
import lombok.*;

@Value
@Builder
public class SendMoneyCommand {

    @NonNull
    AccountId sourceAccountId;

    @NonNull
    AccountId targetAccountId;

    @NonNull
    Money amount;
}
