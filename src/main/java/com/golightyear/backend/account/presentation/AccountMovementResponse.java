package com.golightyear.backend.account.presentation;

import com.golightyear.backend.account.domain.*;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

@Value
public class AccountMovementResponse {
    LocalDateTime     start;
    LocalDateTime     end;
    List<Transaction> transactions;

    public static AccountMovementResponse from(AccountActivity accountActivity) {
        if (accountActivity.hasActivitiesRecorded()) {
            return new AccountMovementResponse(LocalDateTime.ofInstant(accountActivity.getStartTimestamp(), UTC),
                    LocalDateTime.ofInstant(accountActivity.getEndTimestamp(), UTC),
                    accountActivity.getLedgerEntries().stream()
                            .map(Transaction::from)
                            .collect(Collectors.toList()));
        }
        return null;
    }

    @Value
    public static class Transaction {
        LocalDateTime timestamp;
        String        operation;
        Money         amount;

        public static Transaction from(Ledger ledger) {
            return new Transaction(LocalDateTime.ofInstant(ledger.createTime(), UTC), ledger.operation().name(), ledger.amount());
        }
    }
}
