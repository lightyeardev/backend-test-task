package com.golightyear.backend.account.domain;

import java.util.Optional;

public interface BalanceRepository {
    void add(Balance balance);

    Optional<Balance> find(AccountId accountId);
}
