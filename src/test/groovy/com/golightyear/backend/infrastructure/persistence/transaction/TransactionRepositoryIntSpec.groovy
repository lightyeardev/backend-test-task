package com.golightyear.backend.infrastructure.persistence.transaction

import com.golightyear.backend.AbstractIntegrationSpec
import com.golightyear.backend.domain.transaction.TransactionId
import com.golightyear.backend.testData.TransactionTestData
import org.springframework.beans.factory.annotation.Autowired

class TransactionRepositoryIntSpec extends AbstractIntegrationSpec {

    @Autowired
    TransactionRepository transactionRepository

    def "should add and find transaction"() {
        given:
            def transaction = new TransactionTestData().build()

        when:
            transactionRepository.save(transaction)
            def result = transactionRepository.findById(transaction.id())

        then:
            result.isPresent()
            with(result.get()) {
                id() == transaction.id()
                type() == transaction.type()
                accountId() == transaction.accountId()
                money().value() == transaction.money().value()
                money().currency() == transaction.money().currency()
            }
    }

    def "should not find transaction that does not exist"() {
        when:
            def result = transactionRepository.findById(TransactionId.random())

        then:
            result.isEmpty()
    }
}
