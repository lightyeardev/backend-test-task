package com.golightyear.backend.domain.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

import static java.lang.Math.max;
import static java.math.RoundingMode.DOWN;

public class ValueMapKeySerializer extends JsonSerializer<SerializableValue> {

    @Override
    public void serialize(SerializableValue serializableValue, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (serializableValue.value() instanceof BigDecimal) {
            final var scale = max(((BigDecimal) serializableValue.value()).scale(), 1);
            gen.writeFieldName(((BigDecimal) serializableValue.value()).setScale(scale, DOWN).toString());
        } else if (serializableValue.value() instanceof Number) {
            gen.writeFieldName(serializableValue.value().toString());
        } else {
            gen.writeFieldName(serializableValue.value().toString());
        }
    }

    @Override
    public Class<SerializableValue> handledType() {
        return SerializableValue.class;
    }
}
