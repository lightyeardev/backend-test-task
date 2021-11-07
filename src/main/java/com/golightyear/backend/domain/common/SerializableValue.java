package com.golightyear.backend.domain.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(
        using = ValueSerializer.class,
        keyUsing = ValueMapKeySerializer.class
)
public interface SerializableValue<T> {

    T value();

}
