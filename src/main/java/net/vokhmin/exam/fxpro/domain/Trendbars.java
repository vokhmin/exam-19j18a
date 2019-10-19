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

    public static Trendbar newborn(Trendbar bar, Quote quote) {
        return firstborn(
                bar.id.type,
                bornTime(bar.id.type, quote.getTimestamp()),
                quote.getPrice()
        );
    }

    public static Trendbar mutant(Trendbar bar, Quote quote) {
        bar.setClose(quote.price);
        if (quote.price.compareTo(bar.getLow()) < 0) {
            bar.setLow(quote.price);
        } else if (quote.price.compareTo(bar.getHigh()) > 0) {
            bar.setHigh(quote.price);
        }
        return bar;
    }

    public static long bornTime(TrendbarPeriod type, long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .truncatedTo(type.unit)
                .toEpochMilli();
    }

}
