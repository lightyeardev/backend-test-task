package com.golightyear.backend.account.domain.account;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(AccountId accountId) {
        super(String.format("Account with id=%s", accountId.value().toString()));
    }
}
