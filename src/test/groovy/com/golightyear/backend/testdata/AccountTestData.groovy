package com.golightyear.backend.testdata

import com.golightyear.backend.account.domain.Account
import com.golightyear.backend.account.domain.AccountId
import com.golightyear.backend.account.domain.AccountName
import com.golightyear.backend.account.domain.AccountState

import static com.golightyear.backend.account.domain.AccountState.ACTIVE

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