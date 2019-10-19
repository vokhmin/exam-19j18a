package net.vokhmin.exam.fxpro.domain;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.temporal.TemporalUnit;

public enum TrendbarPeriod {

    M1(SECONDS), H1(HOURS), D1(DAYS);

    public final TemporalUnit unit;

    TrendbarPeriod(TemporalUnit unit) {
        this.unit = unit;
    }
}
