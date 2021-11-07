package com.golightyear.backend.testData

import com.golightyear.backend.application.transaction.TransactionFactory
import com.golightyear.backend.domain.account.AccountId
import com.golightyear.backend.domain.transaction.Currency
import com.golightyear.backend.domain.transaction.Money
import com.golightyear.backend.domain.transaction.Transaction
import com.golightyear.backend.domain.transaction.TransactionId
import com.golightyear.backend.domain.transaction.TransactionType

import java.time.Instant

class TransactionTestData {

    TransactionFactory factory = new TransactionFactory()

    TransactionId id = TransactionId.random()
    TransactionType type = TransactionType.CREDIT
    Money money = new Money(BigDecimal.TEN, Currency.EUR)
    AccountId accountId = AccountId.random()

    Transaction build() {
        return factory.create(
                id,
                type,
                accountId,
                money,
                Instant.now()
        )
    }
}