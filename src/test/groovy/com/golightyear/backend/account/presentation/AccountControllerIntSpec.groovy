package com.golightyear.backend.account.presentation

import com.golightyear.backend.AbstractIntegrationSpec
import com.golightyear.backend.account.application.AccountService
import com.golightyear.backend.account.domain.AccountRepository
import com.golightyear.backend.account.domain.AccountId
import com.golightyear.backend.account.domain.AccountName
import com.golightyear.backend.account.domain.BalanceAmount
import com.golightyear.backend.account.domain.BalanceRepository
import com.golightyear.backend.testdata.AccountTestData
import com.golightyear.backend.testdata.BalanceTestData
import org.spockframework.spring.SpringSpy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc

import java.sql.SQLException

import static com.golightyear.backend.account.domain.AccountState.INACTIVE
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
}
