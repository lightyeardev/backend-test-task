package com.golightyear.backend.transaction.domain;

import com.golightyear.backend.common.SerializableValue;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class TransactionAmount implements SerializableValue<BigDecimal> {

    BigDecimal value;

    public TransactionAmount(BigDecimal value) {

        if (BigDecimal.ZERO.compareTo(value) >= 0) {
            throw new IllegalArgumentException("Transaction amount must be bigger than zero");
        }

        this.value = value;
    }
}
