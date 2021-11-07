package com.golightyear.backend.application.lock

import com.golightyear.backend.AbstractIntegrationSpec
import com.golightyear.backend.domain.account.AccountId
import com.golightyear.backend.domain.lock.LockException
import com.golightyear.backend.infrastructure.persistence.account.AccountRepository
import org.springframework.beans.factory.annotation.Autowired

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class AccountLockServiceIntSpec extends AbstractIntegrationSpec {

    private ExecutorService executor = Executors.newFixedThreadPool(10)

    @Autowired
    AccountLockService accountLockService

    @Autowired
    AccountRepository accountRepository

    def "should correctly lock if lock is free"() {
        when:
            accountLockService.executeTransactionWithAccountLockAndReturn(anAccountId(), { return true })

        then:
            notThrown(LockException)
    }

    def "should throw exception if the lock is held"() {
        given:
            executeInNewThread {
                accountLockService.executeTransactionWithAccountLockAndReturn(accountId, {
                    sleep(10_000)
                })
            }
            sleep(1_000)
        when:
            accountLockService.executeTransactionWithAccountLockAndReturn(accountId, {
                return true
            })
        then:
            thrown(LockException)
        where:
            accountId = anAccountId()
    }

    def "should be able to get the lock after release"() {
        given:
            int expectedIncrements = 1000
            int initialNumber = 0

        when:
            def futures = new IntRange(1, expectedIncrements).collect {
                executeInNewThread {
                    return accountLockService.executeTransactionWithAccountLockAndReturn(accountId, {
                        initialNumber += 1
                    })
                }
            }

        then:
            futures.forEach { it.get() }
            initialNumber == expectedIncrements

        where:
            accountId = anAccountId()
    }

    private Future executeInNewThread(Callable callable) {
        return executor.submit(callable)
    }

    private static AccountId anAccountId() {
        return new AccountId(UUID.randomUUID())
    }
}
