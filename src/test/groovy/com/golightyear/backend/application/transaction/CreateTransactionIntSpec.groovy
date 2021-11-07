package com.golightyear.backend.application.transaction

import com.golightyear.backend.AbstractIntegrationSpec
import com.golightyear.backend.domain.account.AccountState
import com.golightyear.backend.domain.balance.BalanceState
import com.golightyear.backend.domain.common.InvalidTransactionException
import com.golightyear.backend.domain.common.NoSuchModelException
import com.golightyear.backend.domain.transaction.Currency
import com.golightyear.backend.domain.transaction.TransactionCreateRequest
import com.golightyear.backend.domain.transaction.TransactionType
import com.golightyear.backend.infrastructure.persistence.account.AccountRepository
import com.golightyear.backend.infrastructure.persistence.balance.BalanceRepository
import com.golightyear.backend.infrastructure.persistence.transaction.TransactionRepository
import com.golightyear.backend.testData.AccountTestData
import com.golightyear.backend.testData.BalanceTestData
import org.springframework.beans.factory.annotation.Autowired

class CreateTransactionIntSpec extends AbstractIntegrationSpec {

    @Autowired
    CreateTransaction createTransaction

    @Autowired
    AccountRepository accountRepository

    @Autowired
    TransactionRepository transactionRepository

    @Autowired
    BalanceRepository balanceRepository

    def "should transfer funds between users"() {
        given: "accounts and balances are present"
            var account = new AccountTestData().build()
            accountRepository.save(account)
            var sourceBalance = new BalanceTestData(accountId: account.id()).build()
            var targetBalance = new BalanceTestData().build()
            balanceRepository.save([sourceBalance, targetBalance])

        when: "transfer fund request is received"
            def request = new TransactionCreateRequest(
                    sourceBalance.id().value(),
                    targetBalance.id().value(),
                    BigDecimal.ONE,
                    Currency.EUR.name()
            )
            createTransaction.execute(account.id().value(), request)

        then: "transactions must be created"
            var debitTransaction = transactionRepository.findByAccountId(sourceBalance.accountId())
            debitTransaction.size() == 1
            debitTransaction.first().type() == TransactionType.DEBIT

            var creditTransaction = transactionRepository.findByAccountId(targetBalance.accountId())
            creditTransaction.size() == 1
            creditTransaction.first().type() == TransactionType.CREDIT

        and: "balances must be updated accordingly"
            balanceRepository.find(sourceBalance.id()).get().money().value() == BigDecimal.valueOf(9L)
            balanceRepository.find(targetBalance.id()).get().money().value() == BigDecimal.valueOf(11L)
    }

    def "should throw when account does not exist" () {
        given:
            def request = new TransactionCreateRequest(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    BigDecimal.ONE,
                    Currency.EUR.name()
            )

        when:
            createTransaction.execute(new AccountTestData().build().id().value(), request)

        then:
            var exception = thrown(NoSuchModelException)
            exception.message.contains("Invalid account")
    }

    def "should throw when account is not active"() {
        given:
            var account = new AccountTestData(state: AccountState.INACTIVE).build()
            accountRepository.save(account)
            var sourceBalance = new BalanceTestData().build()
            var targetBalance = new BalanceTestData().build()
            balanceRepository.save([sourceBalance, targetBalance])

        when:
            def request = new TransactionCreateRequest(
                    sourceBalance.id().value(),
                    targetBalance.id().value(),
                    BigDecimal.ONE,
                    Currency.EUR.name()
            )
            createTransaction.execute(account.id().value(), request)

        then:
            var exception = thrown(InvalidTransactionException)
            exception.message == "Cannot perform transaction as account is inactive"
    }

    def "should throw when target balance is not active"() {
        given:
            var account = new AccountTestData().build()
            accountRepository.save(account)
            var sourceBalance = new BalanceTestData(accountId: account.id()).build()
            var targetBalance = new BalanceTestData(state: BalanceState.INACTIVE).build()
            balanceRepository.save([sourceBalance, targetBalance])

        when:
            def request = new TransactionCreateRequest(
                    sourceBalance.id().value(),
                    targetBalance.id().value(),
                    BigDecimal.ONE,
                    Currency.EUR.name()
            )
            createTransaction.execute(account.id().value(), request)

        then:
            var exception = thrown(InvalidTransactionException)
            exception.message == "Target balance is inactive"
    }

    def "should throw when funds are not sufficient"() {
        given:
            var account = new AccountTestData().build()
            accountRepository.save(account)
            var sourceBalance = new BalanceTestData(accountId: account.id()).build()
            var targetBalance = new BalanceTestData().build()
            balanceRepository.save([sourceBalance, targetBalance])

        when:
            def request = new TransactionCreateRequest(
                    sourceBalance.id().value(),
                    targetBalance.id().value(),
                    BigDecimal.valueOf(100L),
                    Currency.EUR.name()
            )
            createTransaction.execute(account.id().value(), request)

        then:
            var exception = thrown(InvalidTransactionException)
            exception.message == "Funds are not sufficient"
    }
}
