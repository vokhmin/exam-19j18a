package net.vokhmin.exam.fxpro.domain;

import static net.vokhmin.exam.fxpro.RandomUtils.nextBigDecimal;
import static net.vokhmin.exam.fxpro.RandomUtils.nextLong;
import static net.vokhmin.exam.fxpro.RandomUtils.randomEnumValue;

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

    private static Trendbar.ID randomTrendbarId(TrendbarPeriod type) {
        return new Trendbar.ID(
                type,
                randomTimestamp(type));
    }

    private static long randomTimestamp(TrendbarPeriod type) {
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
}
