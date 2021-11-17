package com.golightyear.backend.infrastructure.persistence.balance

import com.golightyear.backend.AbstractIntegrationSpec
import com.golightyear.backend.account.domain.BalanceRepository
import com.golightyear.backend.testdata.BalanceTestData
import org.springframework.beans.factory.annotation.Autowired

class BalanceRepositoryJdbcIntSpec extends AbstractIntegrationSpec {
    @Autowired
    BalanceRepository balanceRepository;

    def "should add and find a balance"() {
        given:
            def balance = new BalanceTestData().build()
        and:
            balanceRepository.add(balance)

        when:
            def existingBalance = balanceRepository.find(balance.accountId())

        then:
            existingBalance.present
            verifyAll(existingBalance.get()) {
                it.id() == balance.id()
                it.accountId() == balance.accountId()
                it.version() == balance.version()
                it.amount().value() == balance.amount().value()
                it.createTime() == balance.createTime()
                it.lastModified() == balance.lastModified()
            }
    }
}
