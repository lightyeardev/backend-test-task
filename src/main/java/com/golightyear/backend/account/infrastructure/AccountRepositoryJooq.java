package com.golightyear.backend.account.infrastructure;

import com.golightyear.backend.account.AccountRepository;
import com.golightyear.backend.account.domain.*;
import com.lightyear.generated.tables.records.AccountRecord;
import com.lightyear.generated.tables.records.LedgerRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.lightyear.generated.Tables.ACCOUNT;
import static com.lightyear.generated.Tables.LEDGER;
import static java.time.ZoneOffset.UTC;
import static org.jooq.impl.DSL.*;

@Component
class AccountRepositoryJooq implements AccountRepository {

    private final DSLContext context;

    public AccountRepositoryJooq(DSLContext context) {
        this.context = context;
    }

    @Override
    public void add(Account account) {
        context.executeInsert(toAccountRecord(account));
    }

    @Override
    public List<Account> findAll() {
        return context.selectFrom(ACCOUNT)
                .fetch(AccountRepositoryJooq::mapAccountRecord);
    }

    @Override
    public Optional<Account> find(AccountId id) {
        LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);
        Optional<Account> account = context.selectFrom(ACCOUNT)
                .where(ACCOUNT.ID.eq(id.value()))
                .fetchOptional(AccountRepositoryJooq::mapAccountRecord);
        return account.map(a -> a.toBuilder()
                .baselineBalance(baselineBalance(id, baselineDate))
                .accountActivity(new AccountActivity(getLedgerEntriesSince(id, baselineDate)))
                .build());
    }

    @Override
    public Optional<Account> findAndLock(AccountId id) {
        LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);
        Optional<Account> account = context.selectFrom(ACCOUNT)
                .where(ACCOUNT.ID.eq(id.value()))
                .forUpdate() // put a 'select for update' lock on row to prevent parallel updates
                .fetchOptional(AccountRepositoryJooq::mapAccountRecord);
        return account.map(a -> a.toBuilder()
                .baselineBalance(baselineBalance(id, baselineDate))
                .accountActivity(new AccountActivity(getLedgerEntriesSince(id, baselineDate)))
                .build());
    }

    @Override
    public void updateActivities(Account account) {
        for (Ledger ledger : account.accountActivity().getLedgerEntries()) {
            context.insertInto(LEDGER)
                    .set(toLedgerRecord(ledger))
                    .onDuplicateKeyIgnore()
                    .execute();
        }
    }

    private static AccountRecord toAccountRecord(Account account) {
        return new AccountRecord(
                account.id().value(),
                account.version(),
                account.name().value(),
                account.state().name(),
                LocalDateTime.ofInstant(account.createTime(), UTC),
                LocalDateTime.ofInstant(account.lastModified(), UTC)
        );
    }

    private static Account mapAccountRecord(AccountRecord record) {
        return Account.builder()
                .id(new AccountId(record.getId()))
                .version(record.getVersion())
                .name(new AccountName(record.getName()))
                .state(AccountState.valueOf(record.getState()))
                .createTime(record.getCreateTime().toInstant(UTC))
                .lastModified(record.getLastModified().toInstant(UTC))
                .build();
    }

    private static LedgerRecord toLedgerRecord(Ledger ledger) {
        return new LedgerRecord(
                ledger.id().value(),
                ledger.debitAccountId() != null ? ledger.debitAccountId().value() : null,
                ledger.creditAccountId() != null ? ledger.creditAccountId().value() : null,
                ledger.operation().name(),
                ledger.amount().value(),
                LocalDateTime.ofInstant(ledger.createTime(), UTC)
        );
    }

    private static Ledger mapLedgerRecord(LedgerRecord record) {
        return Ledger.builder()
                .id(new LedgerId(record.getId()))
                .debitAccountId(record.getDebitaccountid() != null ? new AccountId(record.getDebitaccountid()) : null)
                .creditAccountId(record.getCreditaccountid() != null ? new AccountId(record.getCreditaccountid()) : null)
                .operation(Operation.valueOf(record.getOperation()))
                .amount(Money.of(record.getAmount()))
                .createTime(record.getCreateTime().toInstant(UTC))
                .build();
    }

    private Money baselineBalance(AccountId id, LocalDateTime until) {
        return Money.subtract(
                Money.of(getCreditedBalanceUntil(id, until)),
                Money.of(getDebitedBalanceUntil(id, until)));
    }

    private BigDecimal getCreditedBalanceUntil(AccountId id, LocalDateTime until) {
        BigDecimal sum = context.select(sum(LEDGER.AMOUNT))
                .from(LEDGER)
                .where(LEDGER.CREDITACCOUNTID.eq(id.value()), LEDGER.CREATE_TIME.lessThan(until))
                .fetchOneInto(BigDecimal.class);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    private BigDecimal getDebitedBalanceUntil(AccountId id, LocalDateTime until) {
        BigDecimal sum = context.select(sum(LEDGER.AMOUNT))
                .from(LEDGER)
                .where(LEDGER.DEBITACCOUNTID.eq(id.value()), LEDGER.CREATE_TIME.lessThan(until))
                .fetchOneInto(BigDecimal.class);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    private List<Ledger> getLedgerEntriesSince(AccountId id, LocalDateTime since) {
        return context.selectFrom(LEDGER)
                .where(or(
                                and(LEDGER.DEBITACCOUNTID.eq(id.value()), LEDGER.OPERATION.eq(Operation.WITHDRAWAL.name())),
                                and(LEDGER.CREDITACCOUNTID.eq(id.value()), LEDGER.OPERATION.eq(Operation.DEPOSIT.name()))
                        ),
                        LEDGER.CREATE_TIME.greaterOrEqual(since))
                .fetch(AccountRepositoryJooq::mapLedgerRecord);
    }
}
