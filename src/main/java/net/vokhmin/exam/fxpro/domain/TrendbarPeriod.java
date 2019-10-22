package net.vokhmin.exam.fxpro.domain;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

public enum TrendbarPeriod {

    M1(MINUTES), H1(HOURS), D1(DAYS);

    public final TemporalUnit unit;

    TrendbarPeriod(TemporalUnit unit) {
        this.unit = unit;
    }

    public int milliseconds() {
        return (int) Duration.of(1, unit).toMillis();
    }
}
