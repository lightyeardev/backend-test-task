package com.golightyear.backend.testdata

import com.golightyear.backend.account.domain.account.AccountId
import com.golightyear.backend.account.domain.transaction.Transaction
import com.golightyear.backend.account.domain.transaction.TransactionAmount
import com.golightyear.backend.account.domain.transaction.TransactionId

class TransactionTestData {
    TransactionId id = TransactionId.random()
    AccountId accountId = AccountId.random()
    TransactionAmount amount = new TransactionAmount(BigDecimal.ZERO)

    Transaction build() {
        Transaction.builder()
            .id(id)
            .accountId(accountId)
            .amount(amount)
            .build()
    }
}
