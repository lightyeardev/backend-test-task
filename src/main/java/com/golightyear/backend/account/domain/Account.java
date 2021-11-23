package com.golightyear.backend.account.domain;

import lombok.*;

import java.time.Instant;

import static com.golightyear.backend.account.domain.AccountState.ACTIVE;

@Value
@Builder(toBuilder = true)
public class Account {

    @Builder.Default
    AccountId id = AccountId.random();

    @Builder.Default
    int version = 0;

    @NonNull
    AccountName name;

    @Builder.Default
    Money baselineBalance = Money.ZERO;

    @Builder.Default
    AccountActivity accountActivity = new AccountActivity();

    @Builder.Default
    AccountState state = ACTIVE;

    @Builder.Default
    Instant createTime = Instant.now();

    @Builder.Default
    Instant lastModified = Instant.now();

    public Money calculateBalance() {
        return Money.add(
                this.baselineBalance,
                this.accountActivity.calculateBalance(this.id));
    }

    public boolean withdraw(Money money, AccountId targetAccountId) {

        if (!mayWithdraw(money)) {
            return false;
        }

        Ledger withdrawal = Ledger.builder()
                .debitAccountId(this.id)
                .creditAccountId(targetAccountId)
                .operation(Operation.WITHDRAWAL)
                .amount(money)
                .build();
        this.accountActivity.addToLedger(withdrawal);
        return true;
    }

    private boolean mayWithdraw(Money money) {
        return ACTIVE.equals(this.state)
                && Money.add(
                        this.calculateBalance(),
                        money.negate())
                .isPositiveOrZero();
    }

    public boolean deposit(Money money, AccountId sourceAccountId) {

        if (!mayDeposit()) {
            return false;
        }

        Ledger deposit = Ledger.builder()
                .debitAccountId(sourceAccountId)
                .creditAccountId(this.id)
                .operation(Operation.DEPOSIT)
                .amount(money)
                .build();
        this.accountActivity.addToLedger(deposit);
        return true;
    }

    private boolean mayDeposit() {
        return ACTIVE.equals(this.state);
    }
}
