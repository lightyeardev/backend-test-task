package com.golightyear.backend.account.domain;

import com.golightyear.backend.common.SerializableValue;
import lombok.Value;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Value
public class AccountName implements SerializableValue<String> {

    String value;

    public AccountName(String value) {
        if (isBlank(value)) {
            throw new IllegalArgumentException("Account name can not be blank");
        }

        if (value.length() < 3 || value.length() > 31) {
            throw new IllegalArgumentException("Account name must be greater than 2 and less than 32");
        }

        this.value = value;
    }
}
