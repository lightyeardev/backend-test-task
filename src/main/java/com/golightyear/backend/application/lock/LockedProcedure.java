package com.golightyear.backend.application.lock;

@FunctionalInterface
public interface LockedProcedure<T> {
    T execute();
}