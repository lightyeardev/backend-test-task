package com.golightyear.backend.account.domain.transaction;

import com.golightyear.backend.common.SerializableValue;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class TransactionAmount implements SerializableValue<BigDecimal> {
    BigDecimal value;

    @Override
    public BigDecimal value() {
        return value;
    }
}
