package com.golightyear.backend.testdata

import com.golightyear.backend.account.domain.account.AccountId
import com.golightyear.backend.account.domain.balance.Balance
import com.golightyear.backend.account.domain.balance.BalanceAmount
import com.golightyear.backend.account.domain.balance.BalanceId

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
