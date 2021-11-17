package com.golightyear.backend.account.application;

import com.golightyear.backend.account.domain.*;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;

    @Transactional
    public Account create(String name) {
        var account = Account.builder()
            .name(new AccountName(name))
            .build();

        var balance = Balance.builder()
            .accountId(account.id())
            .build();

        accountRepository.add(account);
        balanceRepository.add(balance);

        return account;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> find(String accountId) {
        return accountRepository.find(new AccountId(accountId));
    }

    public Optional<Balance> balance(String accountId) {
        return balanceRepository.find(new AccountId(accountId));
    }
}
