package com.golightyear.backend.account.persistence;

import com.golightyear.backend.account.AccountRepository;
import com.golightyear.backend.account.domain.Account;
import com.golightyear.backend.account.domain.AccountId;
import com.golightyear.backend.infrastructure.persistence.ApplicationJdbcOperations;
import com.golightyear.backend.infrastructure.persistence.account.AccountRowMapper;

import java.util.List;
import java.util.Optional;

public class AccountRepositoryJdbc implements AccountRepository {
    private final AddAccount addAccount;
    private final FindAccount findAccount;

    public AccountRepositoryJdbc(ApplicationJdbcOperations jdbcOperations) {
        var mapper = new AccountRowMapper();
        this.addAccount = new AddAccount(jdbcOperations);
        this.findAccount = new FindAccount(jdbcOperations, mapper);
    }

    @Override
    public void add(Account account) {
        addAccount.execute(account);
    }

    @Override
    public List<Account> findAll() {
        return List.of();
    }

    @Override
    public Optional<Account> find(AccountId id) {
        return findAccount.execute(id);
    }
}
