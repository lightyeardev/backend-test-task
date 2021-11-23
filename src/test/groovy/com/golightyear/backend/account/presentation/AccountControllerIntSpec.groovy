package com.golightyear.backend.account.presentation

import com.golightyear.backend.AbstractIntegrationSpec
import com.golightyear.backend.account.AccountRepository
import com.golightyear.backend.account.domain.AccountId
import com.golightyear.backend.account.domain.AccountName
import com.golightyear.backend.account.domain.Money
import com.golightyear.backend.testdata.AccountTestData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc

import static com.golightyear.backend.account.domain.AccountState.ACTIVE
import static com.golightyear.backend.account.domain.AccountState.INACTIVE
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
            accountRepository.add(account)

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

    def "should register deposit money command"() {
        given:
        def account = new AccountTestData(
                id: new AccountId("1AAAAAAA-BBBB-CCCC-DDDD-FFFFFFFFFFFF"),
                name: new AccountName("Deposit account"),
                state: ACTIVE,
        ).build()
        accountRepository.add(account)
        def request = """
                {
                    "amount": 100
                }
            """

        expect:
        mvc
                .perform(
                        post('/account/' + account.id().value() + '/deposit')
                                .content(request)
                                .contentType("application/json")
                )
                .andExpect(status().isAccepted())
    }

    def "should register withdraw money command"() {
        given:
        def account = new AccountTestData(
                id: new AccountId("2AAAAAAA-BBBB-CCCC-DDDD-FFFFFFFFFFFF"),
                name: new AccountName("Withdrawal account"),
                state: ACTIVE,
        ).build()
        accountRepository.add(account)
        account.deposit(Money.of(10000), null);
        accountRepository.updateActivities(account);
        def request = """
                {
                    "amount": 100
                }
            """

        expect:
        mvc
                .perform(
                        post('/account/' + account.id().value() + '/withdraw')
                                .content(request)
                                .contentType("application/json")
                )
                .andExpect(status().isAccepted())
    }

    def "should register send money command"() {
        given:
        def senderAccount = new AccountTestData(
                id: new AccountId("3AAAAAAA-BBBB-CCCC-DDDD-FFFFFFFFFFFF"),
                name: new AccountName("Sender account"),
                state: ACTIVE,
        ).build()
        accountRepository.add(senderAccount)
        senderAccount.deposit(Money.of(10000), null);
        accountRepository.updateActivities(senderAccount);
        def recipientAccount = new AccountTestData(
                id: new AccountId("4AAAAAAA-AAAA-CCCC-DDDD-FFFFFFFFFFFF"),
                name: new AccountName("Recipient account"),
                state: ACTIVE,
        ).build()
        accountRepository.add(recipientAccount)
        def request = """
                {
                    "targetAccount": "4AAAAAAA-AAAA-CCCC-DDDD-FFFFFFFFFFFF",
                    "amount": 100
                }
            """

        expect:
        mvc
                .perform(
                        post('/account/' + senderAccount.id().value() + '/send-money')
                                .content(request)
                                .contentType("application/json")
                )
                .andExpect(status().isAccepted())
    }

}
