package com.golightyear.backend.account;

import com.golightyear.backend.account.domain.Account;
import com.golightyear.backend.account.domain.AccountId;
import com.golightyear.backend.account.domain.AccountName;
import com.golightyear.backend.account.domain.AccountState;
import com.lightyear.generated.tables.records.AccountRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.lightyear.generated.Tables.ACCOUNT;
import static java.time.ZoneOffset.UTC;

@Component
public class AccountRepositoryJooq implements AccountRepository {

    private final DSLContext context;

    public AccountRepositoryJooq(DSLContext context) {
        this.context = context;
    }

    @Override
    public void add(Account account) {
        context.executeInsert(toRecord(account));
    }

    @Override
    public List<Account> findAll() {
        return context.selectFrom(ACCOUNT)
                .fetch(AccountRepositoryJooq::mapRecord);
    }

    @Override
    public Optional<Account> find(AccountId id) {
        return context.selectFrom(ACCOUNT)
                .where(ACCOUNT.ID.eq(id.value()))
                .fetchOptional(AccountRepositoryJooq::mapRecord);
    }

    private static AccountRecord toRecord(Account account) {
        return new AccountRecord(
                account.id().value(),
                account.version(),
                account.name().value(),
                account.state().name(),
                LocalDateTime.ofInstant(account.createTime(), UTC),
                LocalDateTime.ofInstant(account.lastModified(), UTC)
        );
    }

    private static Account mapRecord(AccountRecord record) {
        return Account.builder()
                .id(new AccountId(record.getId()))
                .version(record.getVersion())
                .name(new AccountName(record.getName()))
                .state(AccountState.valueOf(record.getState()))
                .createTime(record.getCreateTime().toInstant(UTC))
                .lastModified(record.getLastModified().toInstant(UTC))
                .build();
    }
}
