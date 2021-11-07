package com.golightyear.backend.domain.common;

import lombok.Value;

@Value
public class Resource {
    String name;
    String key;

    public int getNameHashCode() {
        return positiveHashCode(name);
    }

    public int getKeyHashCode() {
        return positiveHashCode(key);
    }

    private int positiveHashCode(String value) {
        return value.hashCode() & 0xfffffff;
    }

}