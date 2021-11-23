package com.golightyear.backend.account.application;

import com.golightyear.backend.account.domain.AccountId;
import com.golightyear.backend.account.domain.Money;
import lombok.*;

@Value
@Builder
public class WithdrawMoneyCommand {

    @NonNull
    AccountId accountId;

    @NonNull
    Money amount;
}
