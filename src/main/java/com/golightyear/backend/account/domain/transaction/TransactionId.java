package com.golightyear.backend.account.domain.transaction;

import com.golightyear.backend.common.SerializableValue;
import lombok.Value;

import java.util.UUID;

@Value
public class TransactionId implements SerializableValue<UUID> {
    UUID value;

    public TransactionId(UUID value) {
        this.value = value;
    }

    public TransactionId(String value) {
        this.value = UUID.fromString(value);
    }

    public static TransactionId random() {
        return new TransactionId(UUID.randomUUID());
    }

    @Override
    public UUID value() {
        return value;
    }
}
