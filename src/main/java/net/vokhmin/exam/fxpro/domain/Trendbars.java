package net.vokhmin.exam.fxpro.domain;

import java.math.BigDecimal;
import java.time.Instant;

public class Trendbars {

    public static Trendbar firstborn(TrendbarPeriod type, long timestamp, BigDecimal price) {
        return new Trendbar(
                new Trendbar.ID(type, timestamp),
                price,
                price,
                price,
                price
        );
    }

    public static Trendbar sibling(Trendbar prev, Quote quote) {
        return firstborn(
                prev.id.type,
                bornTime(prev.id.type, quote.getTimestamp()),
                quote.getPrice()
        );
    }

    public static long bornTime(TrendbarPeriod type, long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .truncatedTo(type.unit)
                .toEpochMilli();
    }

}
