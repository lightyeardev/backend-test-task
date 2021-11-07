package com.golightyear.backend.testData

import com.golightyear.backend.domain.account.Account
import com.golightyear.backend.domain.account.AccountId
import com.golightyear.backend.domain.account.AccountName
import com.golightyear.backend.domain.account.AccountState

import static com.golightyear.backend.domain.account.AccountState.ACTIVE

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