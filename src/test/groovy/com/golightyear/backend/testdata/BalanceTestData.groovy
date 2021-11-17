package com.golightyear.backend.testdata

import com.golightyear.backend.account.domain.AccountId
import com.golightyear.backend.account.domain.Balance
import com.golightyear.backend.account.domain.BalanceAmount
import com.golightyear.backend.account.domain.BalanceId

class BalanceTestData {
    BalanceId balanceId = BalanceId.random()
    AccountId accountId = AccountId.random()
    BalanceAmount amount = BalanceAmount.fresh()

    Balance build() {
        Balance.builder()
            .id(balanceId)
            .accountId(accountId)
            .amount(amount)
            .build()
    }
}
