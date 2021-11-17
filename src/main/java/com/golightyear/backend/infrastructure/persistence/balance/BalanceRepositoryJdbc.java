package com.golightyear.backend.infrastructure.persistence.balance;

import com.golightyear.backend.account.domain.account.AccountId;
import com.golightyear.backend.account.domain.balance.Balance;
import com.golightyear.backend.account.domain.balance.BalanceRepository;
import com.golightyear.backend.infrastructure.persistence.ApplicationJdbcOperations;

import java.util.Optional;

public class BalanceRepositoryJdbc implements BalanceRepository {
    private final AddBalance addBalance;
    private final FindBalance findBalance;

    public BalanceRepositoryJdbc(ApplicationJdbcOperations jdbcOperations) {
        var mapper = new BalanceRowMapper();
        this.addBalance = new AddBalance(jdbcOperations);
        this.findBalance = new FindBalance(jdbcOperations, mapper);
    }

    @Override
    public void add(Balance balance) {
        addBalance.execute(balance);
    }

    @Override
    public Optional<Balance> find(AccountId accountId) {
        return findBalance.execute(accountId);
    }
}
