package net.vokhmin.exam.fxpro.domain;

import java.math.BigDecimal;

public class Trendbars {

    public static Trendbar newborn(TrendbarPeriod type, long timestamp, BigDecimal price) {
        return new Trendbar(
                new Trendbar.ID(type, timestamp),
                price,
                price,
                price,
                price
        );
    }

    public static Trendbar newgen(Trendbar prev, Quote quote) {
        return newborn(prev.id.type, quote.getTimestamp(), quote.getPrice());
    }

    public static long trendbarTimestamp() {
        
    }
}
