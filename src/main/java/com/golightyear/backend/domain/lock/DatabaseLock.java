package com.golightyear.backend.domain.lock;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DatabaseLock implements Lock {
    String lockId;
    String namespace;
}