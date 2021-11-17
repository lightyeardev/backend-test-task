package com.golightyear.backend.account.domain.balance;

import com.golightyear.backend.account.domain.account.AccountId;

import java.util.Optional;

public interface BalanceRepository {
    void add(Balance balance);

    Optional<Balance> find(AccountId accountId);
}
