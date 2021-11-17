package com.golightyear.backend.infrastructure.persistence;

import com.golightyear.backend.common.SerializableValue;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.Timestamp;
import java.time.Instant;

@AllArgsConstructor
public class TypeConvertingParameterSource implements SqlParameterSource {
    private final SqlParameterSource source;

    @Override
    public boolean hasValue(String paramName) {
        return source.hasValue(paramName);
    }

    @Override
    public Object getValue(String paramName) throws IllegalArgumentException {
        return mapValue(source.getValue(paramName));
    }

    private Object mapValue(Object value) {
        if (value instanceof Enum<?>) {
            return ((Enum<?>) value).name();
        }

        if (value instanceof Instant) {
            return Timestamp.from((Instant) value);
        }

        if (value instanceof SerializableValue<?>) {
            return ((SerializableValue<?>) value).value();
        }

        return value;
    }

    @Override
    public int getSqlType(String paramName) {
        return source.getSqlType(paramName);
    }

    @Override
    public String getTypeName(String paramName) {
        return source.getTypeName(paramName);
    }
}
