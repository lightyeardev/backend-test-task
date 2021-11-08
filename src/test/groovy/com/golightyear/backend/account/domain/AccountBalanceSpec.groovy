package com.golightyear.backend.account.domain

import spock.lang.Specification

class AccountBalanceSpec extends Specification {

    def "should initialize with balance"() {
        when:
            def balance = new AccountBalance("123.45")

        then:
            noExceptionThrown()
            balance.value() == new BigDecimal("123.45")
    }

    def "should initialize with zero balance"() {
        when:
        def balance = new AccountBalance("0.00")

        then:
        noExceptionThrown()
        balance.value() == BigDecimal.ZERO
    }

    def "should throw error when balance is negative"() {
        when:
            new AccountBalance("-1.23")

        then:
            def exception = thrown(IllegalArgumentException)
            exception.message == "Account balance cannot be negative"
    }

    def "should throw error when initialized with non numeric chars"() {
        when:
        new AccountBalance("no")

        then:
        thrown(NumberFormatException)
    }
}
