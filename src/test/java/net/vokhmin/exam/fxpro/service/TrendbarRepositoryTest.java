package net.vokhmin.exam.fxpro.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

import static net.vokhmin.exam.fxpro.RandomUtils.randomEnumValue;
import static net.vokhmin.exam.fxpro.domain.TestedTrendbars.TRENDBAR_COMPARATOR;
import static net.vokhmin.exam.fxpro.domain.TestedTrendbars.randomTrendbar;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.vokhmin.exam.fxpro.domain.Trendbar;
import net.vokhmin.exam.fxpro.domain.TrendbarPeriod;

public class TrendbarRepositoryTest {

    TrendbarRepository repo;

    @BeforeMethod
    public void setUp() {
        repo = new TrendbarRepository();
    }

    @Test
    public void testFindAllByTimestampBetween() {
        // given:
        final int count = 4;
        final TrendbarPeriod type = randomEnumValue(TrendbarPeriod.class);
        final Trendbar[] bars = IntStream.range(0, count)
                .boxed()
                .map(i -> randomTrendbar(type))
                .sorted(TRENDBAR_COMPARATOR)
                .collect(Collectors.toList())
                .toArray(new Trendbar[count]);
        // when:
        for (final Trendbar bar : bars) {
            repo.save(bar);
        }
        // then:
        assertThat(
                repo.findAllByTimestampBetween(Long.MIN_VALUE, bars[0].id.timestamp),
                emptyIterable()
        );
        assertThat(
                repo.findAllByTimestampBetween(Long.MIN_VALUE, bars[1].id.timestamp),
                contains(bars[0])
        );
        assertThat(
                repo.findAllByTimestampBetween(Long.MIN_VALUE, bars[3].id.timestamp),
                contains(bars[0], bars[1], bars[2])
        );
        assertThat(
                repo.findAllByTimestampBetween(Long.MIN_VALUE, Long.MAX_VALUE),
                contains(bars)
        );
        assertThat(
                repo.findAllByTimestampBetween(bars[0].id.timestamp, Long.MAX_VALUE),
                contains(bars)
        );
        assertThat(
                repo.findAllByTimestampBetween(bars[3].id.timestamp, Long.MAX_VALUE),
                contains(bars[3])
        );
        assertThat(
                repo.findAllByTimestampBetween(bars[1].id.timestamp, Long.MAX_VALUE),
                contains(bars[1], bars[2], bars[3])
        );
        assertThat(
                repo.findAllByTimestampBetween(bars[1].id.timestamp, bars[3].id.timestamp),
                contains(bars[1], bars[2])
        );
    }

    @Test
    public void testFindAllByTimestampGreaterThanEqual() {
        // given:
        final int count = 4;
        final TrendbarPeriod type = randomEnumValue(TrendbarPeriod.class);
        final Trendbar[] bars = IntStream.range(0, count)
                .boxed()
                .map(i -> randomTrendbar(type))
                .sorted(TRENDBAR_COMPARATOR)
                .collect(Collectors.toList())
                .toArray(new Trendbar[count]);
        // when:
        for (final Trendbar bar : bars) {
            repo.save(bar);
        }
        // then:
        assertThat(
                repo.findAllByTimestampGreaterThanEqual(Long.MIN_VALUE),
                contains(bars)
        );
        assertThat(
                repo.findAllByTimestampGreaterThanEqual(bars[0].id.timestamp),
                contains(bars)
        );
        assertThat(
                repo.findAllByTimestampGreaterThanEqual(bars[count - 1].id.timestamp),
                contains(bars[count - 1])
        );
        assertThat(
                repo.findAllByTimestampGreaterThanEqual(bars[count - 2].id.timestamp),
                contains(bars[count - 2], bars[count - 1])
        );
    }

    @Test
    public void successfulSaveAndFind() {
        // given:
        final Trendbar bar = randomTrendbar();
        // when:
        final Trendbar newbie = repo.save(bar);
        // then:
        assertEquals(bar, newbie);
        // and:
        final Optional<Trendbar> saved = repo.findById(bar.id.timestamp);
        // then:
        assertTrue(saved.isPresent());
        assertEquals(bar, saved.get());
        assertEquals(1, repo.count());
    }

    @Test
    public void testSaveAll() {
    }

    @Test
    public void negativeFindById() {
        // given:
        final Trendbar bar = randomTrendbar();
        // when:
        repo.save(bar);
        // then:
        assertFalse(repo.findById(bar.id.timestamp + 1).isPresent());
        assertFalse(repo.findById(bar.id.timestamp - 1).isPresent());
    }

    @Test
    public void testExistsById() {
    }

    @Test
    public void testFindAll() {
    }

    @Test
    public void testFindAllById() {
    }

    @Test
    public void testCount() {
    }

    @Test
    public void testDeleteAll() {
    }
}