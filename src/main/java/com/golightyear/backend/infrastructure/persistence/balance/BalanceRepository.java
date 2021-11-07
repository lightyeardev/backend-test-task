package com.golightyear.backend.infrastructure.persistence.balance;

import com.golightyear.backend.domain.account.AccountId;
import com.golightyear.backend.domain.balance.Balance;
import com.golightyear.backend.domain.balance.BalanceId;

import java.util.List;
import java.util.Optional;

public interface BalanceRepository {

    void save(Balance balance);

    void save(List<Balance> balances);

    Optional<Balance> find(BalanceId balanceId);

    List<Balance> findByAccountId(AccountId accountId);

}
