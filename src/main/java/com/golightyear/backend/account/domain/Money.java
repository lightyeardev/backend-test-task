package com.golightyear.backend.account.domain;

import com.golightyear.backend.common.SerializableValue;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class Money implements SerializableValue<BigDecimal> {

    public static Money ZERO = Money.of(0L);

    @NonNull
    BigDecimal value;

    public boolean isPositiveOrZero() {
        return this.value.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static Money of(long centValue) {
        return new Money(BigDecimal.valueOf(centValue / 100));
    }

    public static Money of(BigDecimal value) {
        return new Money(value);
    }

    public static Money add(Money a, Money b) {
        return new Money(a.value.add(b.value));
    }

    public static Money subtract(Money a, Money b) {
        return new Money(a.value.subtract(b.value));
    }

    public Money minus(Money money) {
        return new Money(this.value.subtract(money.value));
    }

    public Money plus(Money money) {
        return new Money(this.value.add(money.value));
    }

    public Money negate() {
        return new Money(this.value.negate());
    }
}
