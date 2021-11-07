package com.golightyear.backend.domain.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

import static java.lang.Math.max;
import static java.math.RoundingMode.DOWN;

public class ValueSerializer extends JsonSerializer<SerializableValue> {

    @Override
    public void serialize(SerializableValue serializableValue, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (serializableValue.value() instanceof BigDecimal) {
            final var scale = max(((BigDecimal) serializableValue.value()).scale(), 1);
            gen.writeNumber(((BigDecimal) serializableValue.value()).setScale(scale, DOWN).toString());
        } else if (serializableValue.value() instanceof Number) {
            gen.writeNumber(serializableValue.value().toString());
        } else {
            gen.writeString(serializableValue.value().toString());
        }
    }

    @Override
    public Class<SerializableValue> handledType() {
        return SerializableValue.class;
    }
}
