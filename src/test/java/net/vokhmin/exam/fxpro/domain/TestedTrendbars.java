package net.vokhmin.exam.fxpro.domain;

import static org.testng.Assert.assertEquals;

import static net.vokhmin.exam.fxpro.RandomUtils.nextBigDecimal;
import static net.vokhmin.exam.fxpro.RandomUtils.nextLong;
import static net.vokhmin.exam.fxpro.RandomUtils.randomEnumValue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestedTrendbars {

    public static final Comparator<Trendbar> TRENDBAR_COMPARATOR = Comparator.comparingLong(o -> o.id.timestamp);

    private static Trendbar.ID randomTrendbarId() {
        return new Trendbar.ID(
                randomEnumValue(TrendbarPeriod.class),
                nextLong());
    }

    public static Trendbar randomTrendbar(TrendbarPeriod type) {
        return Trendbar.builder()
                .id(randomTrendbarId(type))
                .low(nextBigDecimal())
                .high(nextBigDecimal())
                .open(nextBigDecimal())
                .close(nextBigDecimal())
                .build();
    }

    public static Trendbar randomTrendbar(TrendbarPeriod type, long timestamp) {
        return Trendbar.builder()
                .id(new Trendbar.ID(type, timestamp))
                .low(nextBigDecimal())
                .high(nextBigDecimal())
                .open(nextBigDecimal())
                .close(nextBigDecimal())
                .build();
    }

    public static Trendbar newbornTrendbar(TrendbarPeriod type, long timestamp, BigDecimal price) {
        return Trendbar.builder()
                .id(new Trendbar.ID(type, timestamp))
                .low(price)
                .high(price)
                .open(price)
                .close(price)
                .build();
    }

    public static Trendbar.TrendbarBuilder buildFrom(Trendbar bar) {
        return Trendbar.builder()
                .id(bar.id)
                .low(bar.getLow())
                .high(bar.getHigh())
                .open(bar.getOpen())
                .close(bar.getClose());
    }

    private static Trendbar.ID randomTrendbarId(TrendbarPeriod type) {
        return new Trendbar.ID(
                type,
                randomTrendbarTime(type));
    }

    private static long randomTrendbarTime(TrendbarPeriod type) {
        return Instant.ofEpochMilli(nextLong())
                .truncatedTo(type.unit)
                .toEpochMilli();
    }

    public static Trendbar randomTrendbar() {
        return Trendbar.builder()
                .id(randomTrendbarId())
                .low(nextBigDecimal())
                .high(nextBigDecimal())
                .open(nextBigDecimal())
                .close(nextBigDecimal())
                .build();
    }

    public static Iterable<Trendbar> seriesTrendbar(TrendbarPeriod type, int count) {
        return IntStream.range(0, count)
                .boxed()
                .map(i -> randomTrendbar(type))
                .sorted(TRENDBAR_COMPARATOR)
                .collect(Collectors.toList());
    }

    public static void assertTrendbar(
            Trendbar bar,
            BigDecimal low,
            BigDecimal high,
            BigDecimal open,
            BigDecimal close
    ) {
        final Trendbar.TrendbarBuilder builder = TestedTrendbars.buildFrom(bar);
        if (low != null) {
            builder.low(low);
        }
        if (high != null) {
            builder.high(high);
        }
        if (open != null) {
            builder.open(open);
        }
        if (close != null) {
            builder.close(close);
        }
        assertEquals(bar, builder.build());
    }

    public static void assertTrendbar(
            Trendbar bar,
            TrendbarPeriod type,
            long timestamp,
            BigDecimal low,
            BigDecimal high,
            BigDecimal open,
            BigDecimal close
    ) {
        assertTrendbar(bar, new Trendbar.ID(type, timestamp), low, high, open, close);
    }

    public static void assertTrendbar(
            Trendbar bar,
            TrendbarPeriod type,
            long timestamp,
            Quote low,
            Quote high,
            Quote open,
            Quote close
    ) {
        assertTrendbar(
                bar,
                new Trendbar.ID(type, timestamp),
                getPrice(low),
                getPrice(high),
                getPrice(open),
                getPrice(close)
        );
    }

    private static BigDecimal getPrice(Quote quote) {
        return quote == null ? null : quote.price;
    }

    public static void assertTrendbar(
            Trendbar bar,
            Trendbar.ID id,
            BigDecimal low,
            BigDecimal high,
            BigDecimal open,
            BigDecimal close
    ) {
        final Trendbar.TrendbarBuilder builder = TestedTrendbars.buildFrom(bar);
        if (id != null) {
            builder.id(id);
        }
        if (low != null) {
            builder.low(low);
        }
        if (high != null) {
            builder.high(high);
        }
        if (open != null) {
            builder.open(open);
        }
        if (close != null) {
            builder.close(close);
        }
        assertEquals(bar, builder.build());
    }

}
