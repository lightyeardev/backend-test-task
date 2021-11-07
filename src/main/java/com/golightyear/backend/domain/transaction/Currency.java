package com.golightyear.backend.domain.transaction;

/**
 * I've created a snapshot of this class. Ideally, we would have an exhaustive list of all currency types.
 */
public enum Currency {

    EUR(2),
    USD(2),
    GBP(2),
    ;

    private final int unit;

    Currency(int unit) {
        this.unit = unit;
    }

    public int getUnit() {
        return unit;
    }

}
