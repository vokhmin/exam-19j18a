package net.vokhmin.exam.fxpro.domain;

import static net.vokhmin.exam.fxpro.RandomUtils.nextDouble;
import static net.vokhmin.exam.fxpro.RandomUtils.nextLong;
import static net.vokhmin.exam.fxpro.RandomUtils.randomEnumValue;
import static net.vokhmin.exam.fxpro.domain.Quote.quoteOf;
import static net.vokhmin.exam.fxpro.domain.TestedSymbols.randomSymbol;
import static net.vokhmin.exam.fxpro.domain.TestedTrendbars.assertTrendbar;
import static net.vokhmin.exam.fxpro.domain.TestedTrendbars.newbornTrendbar;
import static net.vokhmin.exam.fxpro.domain.Trendbars.mutant;

import java.math.BigDecimal;
import java.util.function.Supplier;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TrendbarsTest {

    static final double DELTA_LIMIT = 100.;
    final Supplier<BigDecimal> DELTA = () -> randomDelta(DELTA_LIMIT);

    Symbol symbol;

    @BeforeMethod
    public void setUp() {
        symbol = randomSymbol();
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void testFirstborn() {
    }

    @Test
    public void testNewborn() {
    }

    public void testMutant() {
        final BigDecimal open = randomDelta(100_000_000_000_000.);
        final TrendbarPeriod type = randomEnumValue(TrendbarPeriod.class);
        {   // when:
            BigDecimal high = open.add(DELTA.get());
            final Trendbar bar = newbornTrendbar(type, nextLong(), open);
            final Trendbar actual = mutant(
                    bar,
                    new Quote(symbol, nextLong(), high));
            // then:
            assertTrendbar(actual, null, high, open, high);
        }
        {   // when:
            BigDecimal high = open.add(DELTA.get());
            BigDecimal close = open.subtract(DELTA.get());
            final Trendbar bar = newbornTrendbar(type, nextLong(), open);
            final Trendbar actual =
                    mutant(mutant(mutant(
                            bar,
                            quoteOf(symbol, nextLong(), open)),
                            quoteOf(symbol, nextLong(), high)),
                            quoteOf(symbol, nextLong(), close));
            // then:
            assertTrendbar(actual, null, high, open, close);
        }
        {   // when:
            BigDecimal med = open.add(DELTA.get());
            BigDecimal high = med.add(DELTA.get());
            final Trendbar bar = newbornTrendbar(type, nextLong(), open);
            final Trendbar actual =
                    mutant(mutant(mutant(
                            bar,
                            quoteOf(symbol, nextLong(), open)),
                            quoteOf(symbol, nextLong(), med)),
                            quoteOf(symbol, nextLong(), high));
            // then:
            assertTrendbar(actual, null, high, open, high);
        }
        {   // when:
            BigDecimal low = open.subtract(DELTA.get());
            final Trendbar bar = newbornTrendbar(type, nextLong(), open);
            final Trendbar actual = mutant(
                    bar,
                    quoteOf(symbol, nextLong(), low));
            // then:
            assertTrendbar(actual, low, null, open, low);
        }
        {   // when:
            BigDecimal low = open.subtract(DELTA.get());
            BigDecimal close = low.add(DELTA.get());
            final Trendbar bar = newbornTrendbar(type, nextLong(), open);
            final Trendbar actual =
                    mutant(mutant(mutant(
                            bar,
                            quoteOf(symbol, nextLong(), open)),
                            quoteOf(symbol, nextLong(), low)),
                            quoteOf(symbol, nextLong(), close));
            // then:
            assertTrendbar(actual, low, null, open, close);
        }
        {   // when:
            BigDecimal med = open.subtract(DELTA.get());
            BigDecimal low = med.subtract(DELTA.get());
            final Trendbar bar = newbornTrendbar(type, nextLong(), open);
            final Trendbar actual =
                    mutant(mutant(mutant(
                            bar,
                            quoteOf(symbol, nextLong(), open)),
                            quoteOf(symbol, nextLong(), low)),
                            quoteOf(symbol, nextLong(), low));
            // then:
            assertTrendbar(actual, low, null, open, low);
        }
    }

    @Test
    public void testBornTime() {
    }

    private BigDecimal randomDelta(double d) {
        final double r = nextDouble();
        return BigDecimal.valueOf(r == 0. ? d : r);
    }
}