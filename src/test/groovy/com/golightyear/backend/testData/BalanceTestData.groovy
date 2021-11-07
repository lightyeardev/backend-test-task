package com.golightyear.backend.testData

import com.golightyear.backend.domain.account.AccountId
import com.golightyear.backend.domain.balance.Balance
import com.golightyear.backend.domain.balance.BalanceId
import com.golightyear.backend.domain.balance.BalanceState
import com.golightyear.backend.domain.transaction.Currency
import com.golightyear.backend.domain.transaction.Money

import static com.golightyear.backend.domain.balance.BalanceState.ACTIVE

class BalanceTestData {

    BalanceId id = BalanceId.random()
    AccountId accountId = AccountId.random()
    BalanceState state = ACTIVE
    Money money = new Money(BigDecimal.TEN, Currency.EUR)

    Balance build() {
        return Balance.builder()
                .id(id)
                .accountId(accountId)
                .state(state)
                .money(money)
                .build()
    }
}