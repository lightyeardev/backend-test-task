package com.golightyear.backend.testdata

import com.golightyear.backend.account.domain.account.Account
import com.golightyear.backend.account.domain.account.AccountId
import com.golightyear.backend.account.domain.account.AccountName
import com.golightyear.backend.account.domain.account.AccountState

import static com.golightyear.backend.account.domain.account.AccountState.ACTIVE

class AccountTestData {

    AccountId id = AccountId.random()
    AccountName name = new AccountName("some name")
    AccountState state = ACTIVE

    Account build() {
        return Account.builder()
                .id(id)
                .name(name)
                .state(state)
                .build()
    }
}