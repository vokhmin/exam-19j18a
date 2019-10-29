package net.vokhmin.exam.fxpro.service;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

import static net.vokhmin.exam.fxpro.RandomUtils.nextBigDecimal;
import static net.vokhmin.exam.fxpro.domain.Quote.quoteOf;
import static net.vokhmin.exam.fxpro.domain.TestedSymbols.randomSymbol;

import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.vokhmin.exam.fxpro.domain.Quote;
import net.vokhmin.exam.fxpro.domain.Symbol;

@Test
public class QuoteHandlerWorkerTest {

    QuoteHandlerWorker consumer;

    Symbol symbol;
    BlockingQueue<Quote> queue;
    @Mock
    QuoteHandler handler;
    @Captor
    ArgumentCaptor<Quote> quote;

    @BeforeClass
    void setUp() {
        initMocks(this);
        queue = new LinkedBlockingQueue<>();
        consumer = new QuoteHandlerWorker(queue, handler);
        symbol = randomSymbol();
    }

    @AfterClass
    void tearDown() {
    }

    @Test
    public void testRun() throws InterruptedException {
        // given:
        final Thread thread = new Thread(consumer);
        thread.start();
        // when:
        final long timestamp = System.currentTimeMillis();
        final BigDecimal priceA = nextBigDecimal();
        final Quote quoteA = quoteOf(symbol, timestamp, priceA);
        queue.put(quoteA);
        Thread.sleep(500);
        // then:
        verify(handler).handle(quote.capture());
        assertEquals(quoteA, quote.getValue());
    }

}