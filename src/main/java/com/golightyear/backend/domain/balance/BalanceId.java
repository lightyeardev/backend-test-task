package com.golightyear.backend.domain.balance;

import com.golightyear.backend.domain.common.SerializableValue;
import lombok.Value;

import java.util.UUID;

@Value
public class BalanceId implements SerializableValue<UUID> {

    UUID value;

    public BalanceId(UUID value) {
        this.value = value;
    }

    public BalanceId(String value) {
        this.value = UUID.fromString(value);
    }

    public static BalanceId random() {
        return new BalanceId(UUID.randomUUID());
    }

}
