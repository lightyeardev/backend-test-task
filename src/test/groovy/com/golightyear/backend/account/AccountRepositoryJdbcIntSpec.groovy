package com.golightyear.backend.account

import com.golightyear.backend.AbstractIntegrationSpec
import com.golightyear.backend.account.domain.AccountId
import com.golightyear.backend.account.domain.AccountState
import com.golightyear.backend.testdata.AccountTestData
import org.springframework.beans.factory.annotation.Autowired

class AccountRepositoryJdbcIntSpec extends AbstractIntegrationSpec {

    @Autowired
    AccountRepository accountRepository

    def "should add and find account"() {
        given:
            def account = new AccountTestData(state: AccountState.INACTIVE).build()

        when:
            accountRepository.add(account)
            def result = accountRepository.find(account.id())

        then:
            result.isPresent()
            result.get().id() == account.id()
            result.get().name() == account.name()
            result.get().version() == account.version()
            result.get().state() == account.state()
    }

    def "should not find account that does not exist"() {
        when:
            def result = accountRepository.find(AccountId.random())

        then:
            result.isEmpty()
    }
}
