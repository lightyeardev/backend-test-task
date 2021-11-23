package com.golightyear.backend.account.domain;

import lombok.NonNull;

import java.time.Instant;
import java.util.*;

public class AccountActivity {

    /**
     * The list of account activities within this window.
     */
    private final List<Ledger> ledgerEntries;

    public AccountActivity(@NonNull List<Ledger> ledgerEntries) {
        this.ledgerEntries = ledgerEntries;
    }

    public AccountActivity(@NonNull Ledger... ledgerEntries) {
        this.ledgerEntries = new ArrayList<>(List.of(ledgerEntries));
    }

    /**
     * The timestamp of the first activity within this window.
     */
    public Instant getStartTimestamp() {
        return ledgerEntries.stream()
                .min(Comparator.comparing(Ledger::createTime))
                .orElseThrow(IllegalStateException::new)
                .createTime();
    }

    /**
     * The timestamp of the last activity within this window.
     */
    public Instant getEndTimestamp() {
        return ledgerEntries.stream()
                .max(Comparator.comparing(Ledger::createTime))
                .orElseThrow(IllegalStateException::new)
                .createTime();
    }

    public Money calculateBalance(AccountId accountId) {
        Money depositBalance = ledgerEntries.stream()
                .filter(a -> accountId.equals(a.creditAccountId()) && Operation.DEPOSIT.equals(a.operation()))
                .map(Ledger::amount)
                .reduce(Money.ZERO, Money::add);

        Money withdrawalBalance = ledgerEntries.stream()
                .filter(a -> accountId.equals(a.debitAccountId()) && Operation.WITHDRAWAL.equals(a.operation()))
                .map(Ledger::amount)
                .reduce(Money.ZERO, Money::add);

        return Money.add(depositBalance, withdrawalBalance.negate());
    }

    public List<Ledger> getLedgerEntries() {
        return List.copyOf(this.ledgerEntries);
    }

    public void addToLedger(Ledger ledger) {
        this.ledgerEntries.add(ledger);
    }

    public boolean hasActivitiesRecorded() {
        return !ledgerEntries.isEmpty();
    }
}
