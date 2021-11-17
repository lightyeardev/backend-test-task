package com.golightyear.backend.infrastructure.persistence.account;

import com.golightyear.backend.account.domain.account.Account;
import com.golightyear.backend.account.domain.account.AccountId;
import com.golightyear.backend.account.domain.account.AccountRepository;
import com.golightyear.backend.infrastructure.persistence.ApplicationJdbcOperations;

import java.util.List;
import java.util.Optional;

public class AccountRepositoryJdbc implements AccountRepository {
    private final AddAccount addAccount;
    private final FindAccount findAccount;
    private final FindAllAccount findAllAccount;

    public AccountRepositoryJdbc(ApplicationJdbcOperations jdbcOperations) {
        var mapper = new AccountRowMapper();
        this.addAccount = new AddAccount(jdbcOperations);
        this.findAccount = new FindAccount(jdbcOperations, mapper);
        this.findAllAccount = new FindAllAccount(jdbcOperations, mapper);
    }

    @Override
    public void add(Account account) {
        addAccount.execute(account);
    }

    @Override
    public List<Account> findAll() {
        return findAllAccount.execute();
    }

    @Override
    public Optional<Account> find(AccountId id) {
        return findAccount.execute(id);
    }
}
