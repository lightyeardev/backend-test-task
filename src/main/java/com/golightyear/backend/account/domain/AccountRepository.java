package com.golightyear.backend.account.domain;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    void add(Account account);

    List<Account> findAll();

    Optional<Account> find(AccountId id);

}
