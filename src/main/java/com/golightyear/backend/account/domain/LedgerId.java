package com.golightyear.backend.account.domain;

import com.golightyear.backend.common.SerializableValue;
import lombok.Value;

import java.util.UUID;

@Value
public class LedgerId implements SerializableValue<UUID> {

    UUID value;

    public LedgerId(UUID value) {
        this.value = value;
    }

    public LedgerId(String value) {
        this.value = UUID.fromString(value);
    }

    public static LedgerId random() {
        return new LedgerId(UUID.randomUUID());
    }
}
