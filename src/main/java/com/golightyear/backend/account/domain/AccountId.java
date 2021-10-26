package com.golightyear.backend.account.domain;

import com.golightyear.backend.common.SerializableValue;
import lombok.Value;

import java.util.UUID;

@Value
public class AccountId implements SerializableValue<UUID> {

    UUID value;

    public AccountId(UUID value) {
        this.value = value;
    }

    public AccountId(String value) {
        this.value = UUID.fromString(value);
    }

    public static AccountId random() {
        return new AccountId(UUID.randomUUID());
    }

}
