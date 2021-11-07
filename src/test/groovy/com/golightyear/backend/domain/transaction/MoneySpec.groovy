package com.golightyear.backend.domain.transaction

import spock.lang.Specification

import static com.golightyear.backend.domain.transaction.Currency.EUR
import static com.golightyear.backend.domain.transaction.Currency.GBP

class MoneySpec extends Specification {

    def "should initialize value with 2 decimals"() {
        when:
            def money = new Money(initialValue, EUR)

        then:
            money.value == expectedValue

        where:
            initialValue | expectedValue
            0            | 0.00
            1.0          | 1.00
            2.001        | 2.00
            3.005        | 3.01
            4.0049       | 4.00
            -5.005       | -5.01
    }

    def "should add and return a new Money if currencies are same"() {
        given:
            def money = initialValue

        when:
            def result = money.add(subtractedValue)

        then:
            result == finalValue

        where:
            initialValue        | subtractedValue      | finalValue
            new Money(1.0, EUR) | new Money(2.0, EUR)  | new Money(3.0, EUR)
            new Money(1.0, EUR) | new Money(-2.0, EUR) | new Money(-1.0, EUR)
    }

    def "should not add money if currencies are different"() {
        given:
            def money = new Money(1, EUR)

        when:
            money.add(new Money(2, GBP))

        then:
            thrown(IllegalArgumentException)
    }

    def "should subtract and return a new Money if currencies are same"() {
        given:
            def money = initialValue

        when:
            def result = money.subtract(subtractedValue)

        then:
            result == finalValue

        where:
            initialValue        | subtractedValue      | finalValue
            new Money(1.0, EUR) | new Money(2.0, EUR)  | new Money(-1.0, EUR)
            new Money(1.0, EUR) | new Money(-2.0, EUR) | new Money(3.0, EUR)
    }

    def "should not subtract if currencies are different"() {
        given:
            def money = new Money(1, EUR)

        when:
            money.subtract(new Money(2, GBP))

        then:
            thrown(IllegalArgumentException)
    }
}
