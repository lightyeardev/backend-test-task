package com.golightyear.backend.account;

import com.golightyear.backend.account.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account save(String accountName) {
        final var account = Account.builder()
                .name(new AccountName(accountName))
                .build();

        accountRepository.add(account);

        return account;
    }

    public boolean addToBalance(AccountId accountId, AccountBalance balance) {
        return accountRepository.addBalance(accountId, balance);
    }

    public boolean removeFromBalance(AccountId accountId, AccountBalance balance) {
        return accountRepository.removeBalance(accountId, balance);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> find(String id) {
        return accountRepository.find(new AccountId(id));
    }

    // this can be cached
    public boolean isAccountActive(AccountId id) {
        return accountRepository.find(id)
                .map(Account::state)
                .orElse(AccountState.INACTIVE)
                .equals(AccountState.ACTIVE);
    }
}
