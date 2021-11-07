package com.golightyear.backend.domain.account


import spock.lang.Specification

class AccountNameSpec extends Specification {

    def "should initialize with new name"() {
        when:
            def name = new AccountName("some account name")

        then:
            noExceptionThrown()
            name.value() == "some account name"
    }

    def "should throw error when name is null"() {
        when:
            new AccountName(null)

        then:
            def exception = thrown(IllegalArgumentException)
            exception.message == "Account name can not be blank"
    }

    def "should throw error when name is blank"() {
        when:
            new AccountName("             ")

        then:
            def exception = thrown(IllegalArgumentException)
            exception.message == "Account name can not be blank"
    }

    def "should throw error when name is too short"() {
        when:
            new AccountName("x")

        then:
            def exception = thrown(IllegalArgumentException)
            exception.message == "Account name must be greater than 2 and less than 32"
    }

    def "should throw error when name is too long"() {
        when:
            new AccountName("some very very very very very long name")

        then:
            def exception = thrown(IllegalArgumentException)
            exception.message == "Account name must be greater than 2 and less than 32"
    }
}
