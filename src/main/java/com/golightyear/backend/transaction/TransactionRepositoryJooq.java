package com.golightyear.backend.transaction;

import com.golightyear.backend.transaction.domain.Transaction;
import com.lightyear.generated.tables.records.TransactionRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;

@Component
public class TransactionRepositoryJooq implements TransactionRepository {

    private final DSLContext context;

    public TransactionRepositoryJooq(DSLContext context) {
        this.context = context;
    }

    @Override
    public void add(com.golightyear.backend.transaction.domain.Transaction transaction) {
        context.executeInsert(toRecord(transaction));
    }

    private static TransactionRecord toRecord(Transaction transaction) {
        return new TransactionRecord(
                transaction.id().value(),
                transaction.fromAccount().value(),
                transaction.toAccount().value(),
                transaction.amount().value(),
                LocalDateTime.ofInstant(transaction.createTime(), UTC)
        );
    }

}
