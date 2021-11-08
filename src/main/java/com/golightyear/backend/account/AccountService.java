package com.golightyear.backend.account;

import com.golightyear.backend.account.domain.Account;
import com.golightyear.backend.account.domain.AccountId;
import com.golightyear.backend.account.domain.AccountName;
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

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> find(String id) {
        return accountRepository.find(new AccountId(id));
    }
}
