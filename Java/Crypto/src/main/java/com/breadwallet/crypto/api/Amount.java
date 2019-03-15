package com.breadwallet.crypto.api;

import java.math.BigInteger;

public class Amount {

    public final BigInteger value;  // UInt256 *core
    public final Unit unit;
    public final boolean isNegative;

    private Amount (BigInteger value, Unit unit) {
        this.value = value;
        this.unit  = unit;
        this.isNegative = -1 == value.signum();
    }

    public Amount (long value, Unit unit) {
        this (BigInteger.valueOf(value), unit);
    }

    @Override
    public String toString() {
        return String.format("Amount(value = %s, unit = %s, negative = %b)", value, unit, isNegative);
    }
}
