package net.vokhmin.exam.fxpro.integration;

import static java.util.concurrent.TimeUnit.SECONDS;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import static net.vokhmin.exam.fxpro.domain.TrendbarPeriod.D1;
import static net.vokhmin.exam.fxpro.domain.TrendbarPeriod.H1;
import static net.vokhmin.exam.fxpro.domain.TrendbarPeriod.M1;
import static net.vokhmin.exam.fxpro.domain.Trendbars.bornTime;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import lombok.extern.slf4j.Slf4j;
import net.vokhmin.exam.fxpro.TrendbarsConfig;
import net.vokhmin.exam.fxpro.domain.Quote;
import net.vokhmin.exam.fxpro.domain.Symbol;
import net.vokhmin.exam.fxpro.domain.Trendbar;
import net.vokhmin.exam.fxpro.domain.TrendbarPeriod;
import net.vokhmin.exam.fxpro.service.CustomTimeService;
import net.vokhmin.exam.fxpro.service.QuoteHandlerWorker;
import net.vokhmin.exam.fxpro.service.QuoteProducer;
import net.vokhmin.exam.fxpro.service.QuoteProducerFactory;
import net.vokhmin.exam.fxpro.service.QuoteProviders;
import net.vokhmin.exam.fxpro.service.RandomQuotesFactory;
import net.vokhmin.exam.fxpro.service.TrendbarRepository;
import net.vokhmin.exam.fxpro.service.TrendbarServiceFacade;
import net.vokhmin.exam.fxpro.service.TrendbarStorage;

@Slf4j
@Test
@ContextConfiguration(classes = {
        TrendbarsConfig.class
})
public class QuoteProvidersIntegrationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    BlockingQueue<Quote> quotes;
    @Autowired
    TrendbarStorage storage;
    @Autowired
    TrendbarServiceFacade service;
    @Autowired
    QuoteHandlerWorker worker;

    QuoteProviders providers;
    CustomTimeService timeService;
    Symbol symbolA;
    Symbol symbolB;
    Symbol symbolC;
    Symbol symbolD;
    QuoteProducerFactory producerFactory1;
    QuoteProducerFactory producerFactory2;
    QuoteProducerFactory producerFactory3;

    @BeforeMethod
    void setUp() {
        timeService = new CustomTimeService();
        symbolA = new Symbol("SYMBOL#A"); // randomSymbol();
        symbolB = new Symbol("SYMBOL#B"); // randomSymbol();
        symbolC = new Symbol("SYMBOL#C"); // randomSymbol();
        producerFactory1 = new RandomQuotesFactory(List.of(symbolA), timeService);
        producerFactory2 = new RandomQuotesFactory(List.of(symbolB), timeService);
        producerFactory3 = new RandomQuotesFactory(List.of(symbolC), timeService);
        providers = new QuoteProviders(service);
    }

    @Test
    @DirtiesContext
    public void test() throws Exception {
        final Thread thread = new Thread(worker);
        thread.start();
        final long startAt = System.currentTimeMillis();
        timeService.setCurrentTimeMillis(startAt);
        final QuoteProducer producerA = providers.register(producerFactory1);
        final QuoteProducer producerB = providers.register(producerFactory2);
        final QuoteProducer producerC = providers.register(producerFactory3);
        Thread.sleep(1000);
        timeService.setCurrentTimeMillis(startAt + M1.milliseconds());
        await().atMost(1, SECONDS)
                .until(() -> storage.get(symbolA, M1).count(), greaterThan(0L));
        await().atMost(1, SECONDS)
                .until(() -> storage.get(symbolB, M1).count(), greaterThan(0L));
        await().atMost(1, SECONDS)
                .until(() -> storage.get(symbolC, M1).count(), greaterThan(0L));
        providers.unregister(producerC);
        timeService.setCurrentTimeMillis(startAt + H1.milliseconds());
        final Map<TrendbarPeriod, TrendbarRepository> reposA = getSymbolRepos(symbolA);
        final Map<TrendbarPeriod, TrendbarRepository> reposB = getSymbolRepos(symbolB);
        final Map<TrendbarPeriod, TrendbarRepository> reposC = getSymbolRepos(symbolC);
        await().atMost(1, SECONDS)
                .until(() -> reposA.get(H1).count(), greaterThan(0L));
        await().atMost(1, SECONDS)
                .until(() -> reposB.get(H1).count(), greaterThan(0L));
        await().atMost(1, SECONDS)
                .until(() -> reposC.get(H1).count(), equalTo(0L));
        providers.unregister(producerB);
        timeService.setCurrentTimeMillis(startAt + D1.milliseconds());
        await().atMost(1, SECONDS)
                .until(() -> reposA.get(D1).count(), equalTo(1L));
        await().atMost(1, SECONDS)
                .until(() -> reposB.get(D1).count(), equalTo(0L));
        await().atMost(1, SECONDS)
                .until(() -> reposC.get(D1).count(), equalTo(0L));
        assertThat(
                service.getSeries(symbolA, M1, bornTime(M1, startAt), null)
                        .stream()
                        .map(it -> it.id)
                        .collect(Collectors.toList()),
                contains(
                        new Trendbar.ID(M1, bornTime(M1, startAt)),
                        new Trendbar.ID(M1, bornTime(M1, startAt + M1.milliseconds())),
                        new Trendbar.ID(M1, bornTime(M1, startAt) + H1.milliseconds())
                ));
        assertThat(
                service.getSeries(symbolB, M1, bornTime(M1, startAt), null)
                        .stream()
                        .map(it -> it.id)
                        .collect(Collectors.toList()),
                contains(
                        new Trendbar.ID(M1, bornTime(M1, startAt)),
                        new Trendbar.ID(M1, bornTime(M1, startAt + M1.milliseconds()))
                ));
        assertThat(
                service.getSeries(symbolC, M1, bornTime(M1, startAt), null)
                        .stream()
                        .map(it -> it.id)
                        .collect(Collectors.toList()),
                contains(
                        new Trendbar.ID(M1, bornTime(M1, startAt))
                ));
        assertThat(
                service.getSeries(symbolA, H1, bornTime(H1, startAt), null)
                        .stream()
                        .map(it -> it.id)
                        .collect(Collectors.toList()),
                contains(
                        new Trendbar.ID(H1, bornTime(H1, startAt)),
                        new Trendbar.ID(H1, bornTime(H1, startAt) + H1.milliseconds())
                ));
        assertThat(
                service.getSeries(symbolB, H1, bornTime(H1, startAt), null)
                        .stream()
                        .map(it -> it.id)
                        .collect(Collectors.toList()),
                contains(
                        new Trendbar.ID(H1, bornTime(H1, startAt))
                ));
        assertThat(
                service.getSeries(symbolC, H1, bornTime(H1, startAt), null)
                        .stream()
                        .map(it -> it.id)
                        .collect(Collectors.toList()),
                empty()
        );
        assertThat(
                service.getSeries(symbolA, D1, bornTime(D1, startAt), null)
                        .stream()
                        .map(it -> it.id)
                        .collect(Collectors.toList()),
                contains(
                        new Trendbar.ID(D1, bornTime(D1, startAt))
                ));
        assertThat(
                service.getSeries(symbolB, D1, bornTime(D1, startAt), null)
                        .stream()
                        .map(it -> it.id)
                        .collect(Collectors.toList()),
                empty());
        assertThat(
                service.getSeries(symbolC, D1, bornTime(D1, startAt), null)
                        .stream()
                        .map(it -> it.id)
                        .collect(Collectors.toList()),
                empty());
    }

    private Map<TrendbarPeriod, TrendbarRepository> getSymbolRepos(Symbol symbol) {
        final Map<TrendbarPeriod, TrendbarRepository> repos =
                Arrays.stream(TrendbarPeriod.values())
                        .collect(Collectors.toMap(
                                e -> e,
                                e -> storage.get(symbol, e)));
        return repos;
    }

}
