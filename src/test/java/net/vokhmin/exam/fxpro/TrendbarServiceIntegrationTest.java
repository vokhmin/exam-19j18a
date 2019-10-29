package net.vokhmin.exam.fxpro;

import static java.util.concurrent.TimeUnit.SECONDS;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;

import static net.vokhmin.exam.fxpro.RandomUtils.nextBigDecimal;
import static net.vokhmin.exam.fxpro.domain.Quote.quoteOf;
import static net.vokhmin.exam.fxpro.domain.TestedSymbols.randomSymbol;
import static net.vokhmin.exam.fxpro.domain.TestedTrendbars.newbornTrendbar;
import static net.vokhmin.exam.fxpro.domain.TrendbarPeriod.D1;
import static net.vokhmin.exam.fxpro.domain.TrendbarPeriod.H1;
import static net.vokhmin.exam.fxpro.domain.TrendbarPeriod.M1;
import static net.vokhmin.exam.fxpro.domain.Trendbars.bornTime;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import lombok.extern.slf4j.Slf4j;
import net.vokhmin.exam.fxpro.domain.Quote;
import net.vokhmin.exam.fxpro.domain.Symbol;
import net.vokhmin.exam.fxpro.domain.TrendbarPeriod;
import net.vokhmin.exam.fxpro.service.QuoteHandlerWorker;
import net.vokhmin.exam.fxpro.service.TrendbarRepository;
import net.vokhmin.exam.fxpro.service.TrendbarServiceFacade;
import net.vokhmin.exam.fxpro.service.TrendbarStorage;

@Slf4j
@Test
@ContextConfiguration(classes = {
        TrendbarsConfig.class
})
public class TrendbarServiceIntegrationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    BlockingQueue<Quote> quotes;
    @Autowired
    TrendbarStorage storage;
    @Autowired
    TrendbarServiceFacade service;
    @Autowired
    QuoteHandlerWorker worker;

    @Test
    @DirtiesContext
    public void testAsService() {
        log.info("Start TrendbarServiceIntegrationTest ...");
        final Symbol symbolA = randomSymbol();
        final Symbol symbolB = randomSymbol();
        final Map<TrendbarPeriod, TrendbarRepository> reposA = getSymbolRepos(symbolA);
        final Map<TrendbarPeriod, TrendbarRepository> reposB = getSymbolRepos(symbolB);
        final Thread thread = new Thread(worker);
        thread.start();
        final BigDecimal price = nextBigDecimal();
        final long timestamp = System.currentTimeMillis() - D1.milliseconds();
        service.accept(
                quoteOf(symbolA, timestamp, price)
        );
        service.accept(
                quoteOf(symbolB, timestamp, price)
        );
        service.accept(
                quoteOf(symbolB, timestamp + M1.milliseconds(), price)
        );
        service.accept(
                quoteOf(symbolA, timestamp + M1.milliseconds(), price)
        );
        await().atMost(1, SECONDS)
                .until(() -> reposA.get(M1).count(), equalTo(1L));
        await().atMost(1, SECONDS)
                .until(() -> reposB.get(M1).count(), equalTo(1L));
        service.accept(
                quoteOf(symbolA, timestamp + H1.milliseconds(), price)
        );
        await().atMost(1, SECONDS)
                .until(() -> reposA.get(H1).count(), equalTo(1L));
        service.accept(
                quoteOf(symbolA, timestamp + D1.milliseconds(), price)
        );
        await().atMost(1, SECONDS)
                .until(() -> reposA.get(D1).count(), equalTo(1L));
        assertThat(
                service.getSeries(symbolA, M1, bornTime(M1, timestamp), null),
                contains(
                        newbornTrendbar(M1, bornTime(M1, timestamp), price),
                        newbornTrendbar(M1, bornTime(M1, timestamp + M1.milliseconds()), price),
                        newbornTrendbar(M1, bornTime(M1, timestamp) + H1.milliseconds(), price)
                ));
        assertThat(
                service.getSeries(symbolA, H1, bornTime(H1, timestamp), null),
                contains(
                        newbornTrendbar(H1, bornTime(H1, timestamp), price),
                        newbornTrendbar(H1, bornTime(H1, timestamp + H1.milliseconds()), price)
                ));
        assertThat(
                service.getSeries(symbolA, D1, bornTime(D1, timestamp), null),
                contains(
                        newbornTrendbar(D1, bornTime(D1, timestamp), price)
                ));
    }

    private Map<TrendbarPeriod, TrendbarRepository> getSymbolRepos(Symbol symbol) {
        for (TrendbarPeriod type : TrendbarPeriod.values()) {
            assertEquals(
                    service.getSeries(
                            symbol,
                            type,
                            Long.MIN_VALUE,
                            Long.MAX_VALUE
                    ).size(),
                    0
            );
        }
        final Map<TrendbarPeriod, TrendbarRepository> repos =
                Arrays.stream(TrendbarPeriod.values())
                        .collect(Collectors.toMap(
                                e -> e,
                                e -> storage.get(symbol, e)));
        repos.forEach(
                (type, repo) -> assertEquals(repo.count(), 0)
        );
        return repos;
    }

}
