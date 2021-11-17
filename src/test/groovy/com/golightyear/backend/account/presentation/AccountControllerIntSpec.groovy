package com.golightyear.backend.account.presentation

import com.golightyear.backend.AbstractIntegrationSpec
import com.golightyear.backend.account.application.AccountService
import com.golightyear.backend.account.domain.account.AccountId
import com.golightyear.backend.account.domain.account.AccountName
import com.golightyear.backend.account.domain.balance.BalanceRepository
import com.golightyear.backend.account.domain.transaction.TransactionRepository
import com.golightyear.backend.testdata.AccountTestData
import org.spockframework.spring.SpringSpy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc

import static com.golightyear.backend.account.domain.account.AccountState.INACTIVE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AccountControllerIntSpec extends AbstractIntegrationSpec {

    @Autowired
    protected MockMvc mvc

    @SpringSpy
    BalanceRepository balanceRepository

    @Autowired
    AccountService accountService

    @SpringSpy
    TransactionRepository transactionRepository

    def "should throw an exception"() {
        given:
            def request = """
                    {
                        "name": "Lightyear"
                    }
                """
        when:
            mvc
                .perform(
                    post('/account/')
                        .content(request)
                        .contentType("application/json")
                )

        then:
            balanceRepository.add(_) >> { throw new Exception() }
        and:
            thrown(Exception)
            accountService.findAll().size() == 0
    }

    def "should add account"() {
        given:
            def request = """
                {
                    "name": "Lightyear"
                }
            """

        expect:
            mvc
                .perform(
                    post('/account/')
                        .content(request)
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(
                    """
                        {
                            "name": "Lightyear",
                            "state": "ACTIVE"
                        }
                    """)
                )
    }

    def "should find account by id"() {
        given:
            def account = new AccountTestData(
                id: new AccountId("AAAAAAAA-BBBB-CCCC-DDDD-FFFFFFFFFFFF"),
                name: new AccountName("some inactive account"),
                state: INACTIVE,
            ).build()
            accountService.create(account)

        expect:
            mvc
                .perform(get('/account/' + account.id().value()))
                .andExpect(status().isOk())
                .andExpect(content().json(
                    """
                        {
                            "name": "some inactive account",
                            "state": "INACTIVE"
                        }
                    """)
                )
    }

    def "should get balance by account id"() {
        given:
            def account = accountService.create("Lightyear")

        expect:
            mvc
                .perform(get('/account/' + account.id().value() + '/balance'))
                .andExpect(status().isOk())
                .andExpect(content().json(
                """
                    {
                        "id": %s,
                        "amount": 0
                    }
                """.formatted(account.id().value()))
                )
    }

    def "should not debit or credit balances when exception occurs"() {
        given:
            def anAccount = accountService.create("Lightyear")
            def anAccountBalance = balanceRepository.find(anAccount.id()).get()
            balanceRepository.add(anAccountBalance.credit(new BigDecimal(200)))

            def anotherAccount = accountService.create("Go Lightyear")
        and:
            def request = """
                    {
                        "target": "%s",
                        "amount": 100
                    }
                """.formatted(anotherAccount.id().value().toString())
        when:
            mvc
                .perform(
                    post('/account/' + anAccount.id().value() + '/transfer')
                        .content(request)
                        .contentType("application/json")
                )

        then:
            transactionRepository.add(_) >> { throw new Exception() }
        and:
            thrown(Exception)
            balanceRepository.find(anAccount.id()).present
            balanceRepository.find(anAccount.id()).get().amount().value() == 200
    }
}
