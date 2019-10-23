package net.vokhmin.exam.fxpro.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

import static net.vokhmin.exam.fxpro.RandomUtils.nextBigDecimal;
import static net.vokhmin.exam.fxpro.RandomUtils.nextInt;
import static net.vokhmin.exam.fxpro.RandomUtils.nextLong;
import static net.vokhmin.exam.fxpro.domain.TestedTrendbars.assertTrendbar;
import static net.vokhmin.exam.fxpro.domain.TrendbarPeriod.D1;
import static net.vokhmin.exam.fxpro.domain.TrendbarPeriod.H1;
import static net.vokhmin.exam.fxpro.domain.TrendbarPeriod.M1;
import static net.vokhmin.exam.fxpro.domain.Trendbars.bornTime;

import java.math.BigDecimal;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import lombok.extern.slf4j.Slf4j;
import net.vokhmin.exam.fxpro.domain.Quote;
import net.vokhmin.exam.fxpro.domain.Symbol;
import net.vokhmin.exam.fxpro.domain.TestedSymbols;
import net.vokhmin.exam.fxpro.domain.Trendbar;

public class QuoteServiceTest {

    QuoteService service;
    @Mock
    TrendbarStorage series;
    @Mock
    TrendbarRepository repoM1;
    @Mock
    TrendbarRepository repoH1;
    @Mock
    TrendbarRepository repoD1;
    @Captor
    ArgumentCaptor<Trendbar> barM1;
    @Captor
    ArgumentCaptor<Trendbar> barM2;
    @Captor
    ArgumentCaptor<Trendbar> barM3;
    @Captor
    ArgumentCaptor<Trendbar> barH1;
    @Captor
    ArgumentCaptor<Trendbar> barH2;
    @Captor
    ArgumentCaptor<Trendbar> barD1;

    Symbol symbol;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        service = new QuoteService(series);
        symbol = TestedSymbols.randomSymbol();
        when(series.get(symbol, M1)).thenReturn(repoM1);
        when(series.get(symbol, H1)).thenReturn(repoH1);
        when(series.get(symbol, D1)).thenReturn(repoD1);
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void testHandle() {
        // given:
        final long timestamp = bornTime(D1, nextLong());
        final BigDecimal price = nextBigDecimal();
        final Quote quoteA = new Quote(
                symbol,
                timestamp,
                price);
        final Quote quoteB = new Quote(
                symbol,
                timestamp + 1,
                price.add(BigDecimal.ONE));
        final Quote quoteC = new Quote(
                symbol,
                timestamp + 2,
                price.subtract(BigDecimal.ONE));
        final Quote quoteM1 = new Quote(
                symbol,
                timestamp + nextInt(M1.milliseconds()) + M1.milliseconds(),
                nextBigDecimal());
        {// when:
            service.handle(quoteA, quoteB, quoteC, quoteM1);
            // then:
            verify(repoM1, times(1)).save(barM1.capture());
            assertTrendbar(barM1.getAllValues().get(0), M1, timestamp, quoteC.price, quoteB.price, quoteA.price, quoteC.price);
            verifyNoMoreInteractions(repoM1, repoH1, repoD1);
        }
        final Quote quoteH1 = new Quote(
                symbol,
                timestamp + H1.milliseconds() + nextInt(M1.milliseconds()),
                nextBigDecimal());
        {// and:
            service.handle(quoteA, quoteB, quoteC, quoteH1);
            // then:
            verify(repoM1, times(2)).save(barM2.capture());
            assertTrendbar(barM2.getAllValues().get(1), M1, timestamp + M1.milliseconds(), quoteM1, quoteM1, quoteM1, quoteM1);
            verify(repoH1).save(barH1.capture());
            assertTrendbar(barH1.getAllValues().get(0), H1, timestamp, quoteC.price, quoteB.price, quoteA.price, quoteC.price);
            verifyNoMoreInteractions(repoM1, repoH1, repoD1);
        }
        final Quote quoteD1 = new Quote(
                symbol,
                timestamp + nextInt(D1.milliseconds()) + D1.milliseconds(),
                nextBigDecimal());
        {// and:
            service.handle(quoteA, quoteB, quoteC, quoteD1);
            // then:
            verify(repoM1, times(3)).save(barM3.capture());
            assertTrendbar(barM3.getAllValues().get(2), M1, timestamp + H1.milliseconds(), quoteH1, quoteH1, quoteH1, quoteH1);
            verify(repoH1, times(2)).save(barH2.capture());
            assertTrendbar(barH2.getAllValues().get(1), H1, timestamp + H1.milliseconds(), quoteH1, quoteH1, quoteH1, quoteH1);
            verify(repoD1).save(barD1.capture());
            final Trendbar actual = barD1.getValue();
            assertTrendbar(actual, D1, timestamp, quoteC.price, quoteB.price, quoteA.price, quoteC.price);
            verifyNoMoreInteractions(repoM1, repoH1, repoD1);
        }
    }

    @Test
    public void testHandleM1() {
        // given:
        final long timestamp = bornTime(D1, nextLong());
        final Quote quoteA = new Quote(
                symbol,
                timestamp + nextInt(M1.milliseconds()),
                nextBigDecimal());
        final Quote quoteB = new Quote(
                symbol,
                timestamp + nextInt(M1.milliseconds()) + M1.milliseconds(),
                nextBigDecimal());
        // when:
        service.handle(quoteA, quoteB);
        // then:
        verify(repoM1).save(barM1.capture());
        assertEquals(
                barM1.getValue().id,
                new Trendbar.ID(M1, timestamp)
        );
    }

    @Test
    public void testHandleH1() {
        // given:
        final long timestamp = bornTime(D1, nextLong());
        final Quote quoteA = new Quote(symbol, timestamp + nextInt(H1.milliseconds()), nextBigDecimal());
        final Quote quoteB = new Quote(symbol, timestamp + H1.milliseconds(), nextBigDecimal());
        // when:
        service.handle(quoteA, quoteB);
        // then:
        verify(repoM1).save(barM1.capture());
        verify(repoH1).save(barH1.capture());
        assertEquals(
                barM1.getValue().id,
                new Trendbar.ID(M1, bornTime(M1, quoteA.timestamp))
        );
        assertEquals(
                barH1.getValue().id,
                new Trendbar.ID(H1, timestamp)
        );
    }

    @Test
    public void testHandleD1() {
        // given:
        final long timestamp = bornTime(D1, nextLong());
        final Quote quoteA = new Quote(symbol, timestamp + nextInt(D1.milliseconds()), nextBigDecimal());
        final Quote quoteB = new Quote(symbol, timestamp + D1.milliseconds(), nextBigDecimal());
        // when:
        service.handle(quoteA, quoteB);
        // then:
        verify(repoM1).save(barM1.capture());
        verify(repoH1).save(barH1.capture());
        verify(repoD1).save(barD1.capture());
        assertEquals(
                barM1.getValue().id,
                new Trendbar.ID(M1, bornTime(M1, quoteA.timestamp))
        );
        assertEquals(
                barH1.getValue().id,
                new Trendbar.ID(H1, bornTime(H1, quoteA.timestamp))
        );
        assertEquals(
                barD1.getValue().id,
                new Trendbar.ID(D1, timestamp)
        );
    }

    @Test(enabled = false)
    public void testHandleBelated() {
    }

    @Slf4j
    public static class CustomTrendbarRepo extends TrendbarRepository {
        @Override
        public <T extends Trendbar> T save(T tbar) {
            log.info("save {}", tbar);
            return super.save(tbar);
        }
    }
}