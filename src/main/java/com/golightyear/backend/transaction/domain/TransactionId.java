package com.golightyear.backend.transaction.domain;

import com.golightyear.backend.common.SerializableValue;
import lombok.Value;

import java.util.UUID;

@Value
public class TransactionId implements SerializableValue<UUID> {

    UUID value;

    public TransactionId(UUID value) {
        this.value = value;
    }

    public static TransactionId random() {
        return new TransactionId(UUID.randomUUID());
    }

}
