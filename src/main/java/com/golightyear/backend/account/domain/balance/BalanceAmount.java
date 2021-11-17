package com.golightyear.backend.account.domain.balance;

import com.golightyear.backend.common.SerializableValue;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class BalanceAmount implements SerializableValue<BigDecimal> {
    BigDecimal value;

    public BalanceAmount(BigDecimal value) {
        this.value = value;
    }

    public static BalanceAmount fresh() {
        return new BalanceAmount(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal value() {
        return value;
    }

    public BalanceAmount add(BigDecimal amount) {
        return new BalanceAmount(this.value.add(amount));
    }

    public BalanceAmount subtract(BigDecimal amount) {
        return new BalanceAmount(this.value.subtract(amount));
    }
}
