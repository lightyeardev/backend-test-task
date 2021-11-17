package com.golightyear.backend.account.presentation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.golightyear.backend.account.domain.account.AccountId;
import com.golightyear.backend.account.domain.balance.BalanceAmount;
import com.golightyear.backend.account.domain.transaction.TransactionId;
import com.golightyear.backend.account.domain.transaction.TransactionResult;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class TransferResponse {
    TransactionId transactionId;
    AccountId accountId;
    BalanceAmount amount;
    Instant createTime;

    public static TransferResponse from(TransactionResult result) {
        return  new TransferResponse(
            result.id(),
            result.accountId(),
            result.balanceAmount(),
            result.createTime()
        );
    }
}
