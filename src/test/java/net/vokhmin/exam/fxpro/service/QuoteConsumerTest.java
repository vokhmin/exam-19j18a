package net.vokhmin.exam.fxpro.service;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

import static net.vokhmin.exam.fxpro.RandomUtils.nextBigDecimal;
import static net.vokhmin.exam.fxpro.domain.Quote.quoteOf;
import static net.vokhmin.exam.fxpro.domain.TestedSymbols.randomSymbol;

import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Flow;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SubmissionPublisher;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.vokhmin.exam.fxpro.domain.Quote;
import net.vokhmin.exam.fxpro.domain.Symbol;

@Test
public class QuoteConsumerTest {

    QuoteConsumer consumer;

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
        consumer = new QuoteConsumer(queue, handler);
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

    @Test
    public void test() throws InterruptedException {
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        publisher.subscribe(new PrintSubscriber());
        System.out.println("Submitting items...");
        for (int i = 0; i < 10; i++) {
            publisher.submit(i);
        }
        Thread.sleep(1000);
        publisher.close();
    }

    public class PrintSubscriber implements Flow.Subscriber<Integer> {
        private Flow.Subscription subscription;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(1);
        }

        @Override
        public void onNext(Integer item) {
            System.out.println("Received item: " + item);
            subscription.request(1);
        }

        @Override
        public void onError(Throwable error) {
            System.out.println("Error occurred: " + error.getMessage());
        }

        @Override
        public void onComplete() {
            System.out.println("PrintSubscriber is complete");
        }
    }

}