package com.golightyear.backend.account.domain;

import com.golightyear.backend.common.SerializableValue;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class AccountBalance implements SerializableValue<BigDecimal> {

    BigDecimal value;

    public AccountBalance(String value) {
        this(new BigDecimal(value));
    }

    public AccountBalance(BigDecimal value) {

        if (BigDecimal.ZERO.compareTo(value) > 0) {
            throw new IllegalArgumentException("Account balance cannot be negative");
        }

        this.value = value;
    }
}
