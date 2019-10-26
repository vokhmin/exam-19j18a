package net.vokhmin.exam.fxpro.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.AssertJUnit.assertEquals;

import static net.vokhmin.exam.fxpro.RandomUtils.nextInt;
import static net.vokhmin.exam.fxpro.RandomUtils.nextLong;
import static net.vokhmin.exam.fxpro.RandomUtils.randomEnumValue;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.mockito.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import lombok.extern.slf4j.Slf4j;
import net.vokhmin.exam.fxpro.domain.Quote;
import net.vokhmin.exam.fxpro.domain.Symbol;
import net.vokhmin.exam.fxpro.domain.TestedSymbols;
import net.vokhmin.exam.fxpro.domain.TestedTrendbars;
import net.vokhmin.exam.fxpro.domain.Trendbar;
import net.vokhmin.exam.fxpro.domain.TrendbarPeriod;

@Slf4j
public class TrendbarServiceTest {

    TrendbarService service;
    BlockingQueue<Quote> queue = new LinkedBlockingQueue<>();
    @Mock
    TrendbarStorage series;
    @Mock
    TrendbarRepository repo;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        service = new TrendbarService(queue);
        service.setSeries(series);
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void testGetSeriesGreaterThanEqual() {
        // given:
        final Symbol symbol = TestedSymbols.randomSymbol();
        final TrendbarPeriod type = randomEnumValue(TrendbarPeriod.class);
        final Long from = nextLong();
        // when:
        final Iterable<Trendbar> expected = TestedTrendbars.seriesTrendbar(type, nextInt(5));
        when(series.get(symbol, type)).thenReturn(repo);
        when(repo.findAllByTimestampGreaterThanEqual(from)).thenReturn(expected);
        final List<Trendbar> actual = service.getSeries(symbol, type, from, null);
        // then:
        assertEquals(actual, expected);
        verify(series).get(symbol, type);
        verify(repo).findAllByTimestampGreaterThanEqual(from);
        verifyNoMoreInteractions(series, repo);
    }

    @Test
    public void testGetSeriesBetween() {
        // given:
        final Symbol symbol = TestedSymbols.randomSymbol();
        final TrendbarPeriod type = randomEnumValue(TrendbarPeriod.class);
        final Long from = nextLong();
        final Long upTo = nextLong();
        // when:
        final Iterable<Trendbar> expected = TestedTrendbars.seriesTrendbar(type, nextInt(5));
        when(series.get(symbol, type)).thenReturn(repo);
        when(repo.findAllByTimestampBetween(from, upTo)).thenReturn(expected);
        final List<Trendbar> actual = service.getSeries(symbol, type, from, upTo);
        // then:
        assertEquals(actual, expected);
        verify(series).get(symbol, type);
        verify(repo).findAllByTimestampBetween(from, upTo);
        verifyNoMoreInteractions(series, repo);
    }

}