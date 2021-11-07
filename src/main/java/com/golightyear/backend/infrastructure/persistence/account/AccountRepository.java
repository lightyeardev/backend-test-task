package com.golightyear.backend.infrastructure.persistence.account;

import com.golightyear.backend.domain.account.Account;
import com.golightyear.backend.domain.account.AccountId;

import java.util.Optional;

public interface AccountRepository {

    void save(Account account);

    Optional<Account> find(AccountId id);

}
