package com.golightyear.backend.domain.transaction;

import lombok.NonNull;
import lombok.Value;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

@Value
public class Money {

    BigDecimal value;
    Currency currency;

    public Money(@NonNull BigDecimal value, @NonNull Currency currency) {
        this.value = value.setScale(currency.getUnit(), HALF_UP);
        this.currency = currency;
    }

    public Money add(Money money) {
        validateCurrency(money);
        return new Money(this.value.add(money.value), this.currency);
    }

    public Money subtract(Money money) {
        validateCurrency(money);
        return new Money(this.value.subtract(money.value), this.currency);
    }

    public boolean lessThan(Money money) {
        return this.value.compareTo(money.value) < 0;
    }

    private void validateCurrency(Money money) {
        Preconditions.checkArgument(this.currency == money.currency, "Currencies should be same");
    }
}
