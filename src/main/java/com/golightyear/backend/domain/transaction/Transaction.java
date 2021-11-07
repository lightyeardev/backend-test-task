package com.golightyear.backend.domain.transaction;

import com.golightyear.backend.domain.account.AccountId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Getter
@AllArgsConstructor
@Slf4j
public abstract class Transaction {

    @NonNull
    TransactionId id;

    @NonNull
    TransactionType type;

    @NonNull
    AccountId accountId;

    @NonNull
    Money money;

    @NonNull
    Instant createTime;

    public void created() {
        log.info("{} {} {} {}",
                kv("event", "TRANSACTION_CREATED"),
                kv("accountId", accountId().value()),
                kv("transactionId", id()),
                kv("transactionType", type())
        );
    }
}
