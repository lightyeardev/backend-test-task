package com.golightyear.backend.presentation

import com.golightyear.backend.AbstractIntegrationSpec
import com.golightyear.backend.testData.AccountTestData
import com.golightyear.backend.domain.account.AccountId
import com.golightyear.backend.domain.account.AccountName
import com.golightyear.backend.infrastructure.persistence.account.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc

import static com.golightyear.backend.domain.account.AccountState.INACTIVE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AccountControllerIntSpec extends AbstractIntegrationSpec {

    @Autowired
    protected MockMvc mvc

    @Autowired
    AccountRepository accountRepository

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
                            post('/v1/accounts/')
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
            accountRepository.save(account)

        expect:
            mvc
                    .perform(get('/v1/accounts/' + account.id().value()))
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

}
