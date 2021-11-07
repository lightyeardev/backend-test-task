package com.golightyear.backend.presentation

import com.golightyear.backend.AbstractIntegrationSpec
import com.golightyear.backend.application.transaction.CreateTransaction
import com.golightyear.backend.domain.account.AccountState
import com.golightyear.backend.domain.balance.BalanceState
import com.golightyear.backend.domain.transaction.Currency
import com.golightyear.backend.infrastructure.persistence.account.AccountRepository
import com.golightyear.backend.infrastructure.persistence.balance.BalanceRepository
import com.golightyear.backend.infrastructure.persistence.transaction.TransactionRepository
import com.golightyear.backend.testData.AccountTestData
import com.golightyear.backend.testData.BalanceTestData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc

import static org.hamcrest.Matchers.containsString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CreateTransactionControllerIntSpec extends AbstractIntegrationSpec {

    @Autowired
    protected MockMvc mvc

    @Autowired
    AccountRepository accountRepository

    @Autowired
    BalanceRepository balanceRepository

    @Autowired
    TransactionRepository transactionRepository

    @Autowired
    CreateTransaction transactionService

    def "should succeed to transfer funds for valid request"() {
        given:
            var account = new AccountTestData().build()
            accountRepository.save(account)
            var sourceBalance = new BalanceTestData(accountId: account.id()).build()
            var targetBalance = new BalanceTestData().build()
            balanceRepository.save([sourceBalance, targetBalance])

        when:
            def request = createRequest(sourceBalance, targetBalance)

        then:
            balanceRepository.find(sourceBalance.id()).get().money().value() == BigDecimal.TEN
            balanceRepository.find(targetBalance.id()).get().money().value() == BigDecimal.TEN

        when:
            mvc
                    .perform(
                            post("/v1/accounts/${account.id().value()}/transactions")
                                    .content(request)
                                    .contentType("application/json")
                    )
                    .andExpect(status().isOk())

        then:
            balanceRepository.find(sourceBalance.id()).get().money().value() == BigDecimal.valueOf(9L)
            balanceRepository.find(targetBalance.id()).get().money().value() == BigDecimal.valueOf(11L)
    }

    def "should fail if account is inactive"() {
        given:
            var account = new AccountTestData(state: AccountState.INACTIVE).build()
            accountRepository.save(account)
            var sourceBalance = new BalanceTestData().build()
            var targetBalance = new BalanceTestData().build()
            balanceRepository.save([sourceBalance, targetBalance])

        when:
            def request = createRequest(sourceBalance, targetBalance)

        then:
            mvc
                    .perform(
                            post("/v1/accounts/${account.id().value()}/transactions")
                                    .content(request)
                                    .contentType("application/json")
                    )
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(status().reason(containsString("Cannot perform transaction as account is inactive")))
    }

    def "should fail if source balance is inactive"() {
        given:
            var account = new AccountTestData().build()
            accountRepository.save(account)
            var sourceBalance = new BalanceTestData(accountId: account.id(), state: BalanceState.INACTIVE).build()
            var targetBalance = new BalanceTestData().build()
            balanceRepository.save([sourceBalance, targetBalance])

        when:
            def request = createRequest(sourceBalance, targetBalance)

        then:
            mvc
                    .perform(
                            post("/v1/accounts/${account.id().value()}/transactions")
                                    .content(request)
                                    .contentType("application/json")
                    )
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(status().reason(containsString("Source balance is inactive")))
    }

    def "should fail if target balance is inactive"() {
        given:
            var account = new AccountTestData().build()
            accountRepository.save(account)
            var sourceBalance = new BalanceTestData(accountId: account.id()).build()
            var targetBalance = new BalanceTestData(state: BalanceState.INACTIVE).build()
            balanceRepository.save([sourceBalance, targetBalance])

        when:
            def request = createRequest(sourceBalance, targetBalance)

        then:
            mvc
                    .perform(
                            post("/v1/accounts/${account.id().value()}/transactions")
                                    .content(request)
                                    .contentType("application/json")
                    )
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(status().reason(containsString("Target balance is inactive")))
    }

    def "should fail if funds are not sufficient"() {
        given:
            var account = new AccountTestData().build()
            accountRepository.save(account)
            var sourceBalance = new BalanceTestData(accountId: account.id()).build()
            var targetBalance = new BalanceTestData().build()
            balanceRepository.save([sourceBalance, targetBalance])

        when:
            def request = createRequest(sourceBalance, targetBalance, BigDecimal.valueOf(100L))

        then:
            mvc
                    .perform(
                            post("/v1/accounts/${account.id().value()}/transactions")
                                    .content(request)
                                    .contentType("application/json")
                    )
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(status().reason(containsString("Funds are not sufficient")))
    }

    def "should fail if source and target balances are same"() {
        given:
            var account = new AccountTestData().build()
            accountRepository.save(account)
            var sourceBalance = new BalanceTestData(accountId: account.id()).build()
            balanceRepository.save([sourceBalance])

        when:
            def request = createRequest(sourceBalance, sourceBalance)

        then:
            mvc
                    .perform(
                            post("/v1/accounts/${account.id().value()}/transactions")
                                    .content(request)
                                    .contentType("application/json")
                    )
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(status().reason(containsString("Cannot send money to self")))
    }

    static def createRequest(sourceBalance, targetBalance, amount = BigDecimal.ONE) {
        def request = """
                {
                    "sourceBalanceId": "%s",
                    "targetBalanceId": "%s",
                    "amount": "%s",
                    "currency": "%s"
                }
            """.formatted(
                sourceBalance.id().value(),
                targetBalance.id().value(),
                amount,
                Currency.EUR.name()
        )
        request
    }
}
