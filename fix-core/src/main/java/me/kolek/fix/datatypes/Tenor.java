package me.kolek.fix.datatypes;

import java.time.temporal.ChronoUnit;

public class Tenor {
    private final long magnitude;
    private final ChronoUnit unit;

    public Tenor(long magnitude, ChronoUnit unit) {
        this.magnitude = magnitude;
        this.unit = unit;
    }

    public long getMagnitude() {
        return magnitude;
    }

    public ChronoUnit getUnit() {
        return unit;
    }
}
