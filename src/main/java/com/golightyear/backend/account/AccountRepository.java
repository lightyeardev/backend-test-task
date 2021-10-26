package com.golightyear.backend.account;

import com.golightyear.backend.account.domain.Account;
import com.golightyear.backend.account.domain.AccountId;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    void add(Account account);

    List<Account> findAll();

    Optional<Account> find(AccountId id);

}
