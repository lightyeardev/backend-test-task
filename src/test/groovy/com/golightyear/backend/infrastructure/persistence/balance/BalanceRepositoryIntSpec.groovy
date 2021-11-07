package com.golightyear.backend.infrastructure.persistence.balance

import com.golightyear.backend.AbstractIntegrationSpec
import com.golightyear.backend.domain.balance.BalanceId
import com.golightyear.backend.testData.BalanceTestData
import org.springframework.beans.factory.annotation.Autowired

class BalanceRepositoryIntSpec extends AbstractIntegrationSpec {

    @Autowired
    BalanceRepository balanceRepository

    def "should add and find balance"() {
        given:
            def balance = new BalanceTestData().build()

        when:
            balanceRepository.save([balance])
            def result = balanceRepository.find(balance.id())

        then:
            result.isPresent()
            with(result.get()) {
                id() == balance.id()
                version() == balance.version()
                state() == balance.state()
                accountId() == balance.accountId()
                money().value() == balance.money().value()
                money().currency() == balance.money().currency()
            }
    }

    def "should not find balance that does not exist"() {
        when:
            def result = balanceRepository.find(BalanceId.random())

        then:
            result.isEmpty()
    }

}
