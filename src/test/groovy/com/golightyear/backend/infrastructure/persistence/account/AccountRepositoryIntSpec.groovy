package com.golightyear.backend.infrastructure.persistence.account

import com.golightyear.backend.AbstractIntegrationSpec
import com.golightyear.backend.domain.account.AccountId
import com.golightyear.backend.infrastructure.persistence.balance.BalanceRepository
import com.golightyear.backend.testData.AccountTestData
import com.golightyear.backend.testData.BalanceTestData
import org.springframework.beans.factory.annotation.Autowired

class AccountRepositoryIntSpec extends AbstractIntegrationSpec {

    @Autowired
    AccountRepository accountRepository

    @Autowired
    BalanceRepository balanceRepository

    def "should add and find account"() {
        given:
            def account = new AccountTestData().build()

        when:
            accountRepository.save(account)
            def result = accountRepository.find(account.id())

        then:
            result.isPresent()
            with(result.get()) {
                id() == account.id()
                name() == account.name()
                version() == account.version()
                state() == account.state()
                balances() == []
            }
    }

    def "should populate balances correctly"() {
        given:
            def account = new AccountTestData().build()
            accountRepository.save(account)

        and:
            def balance = new BalanceTestData(accountId: account.id()).build()
            balanceRepository.save([balance])

        when:
            def result = accountRepository.find(account.id())

        then:
            result.isPresent()
            with(result.get()) {
                id() == account.id()
                name() == account.name()
                version() == account.version()
                state() == account.state()
                balances().size() == 1
                balances().first().id() == balance.id()
            }
    }

    def "should not find account that does not exist"() {
        when:
            def result = accountRepository.find(AccountId.random())

        then:
            result.isEmpty()
    }
}
