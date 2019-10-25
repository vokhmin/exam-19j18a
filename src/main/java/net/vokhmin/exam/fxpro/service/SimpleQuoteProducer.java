package net.vokhmin.exam.fxpro.service;

import static java.math.BigDecimal.valueOf;

import static net.vokhmin.exam.fxpro.domain.Quote.quoteOf;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.extern.slf4j.Slf4j;
import net.vokhmin.exam.fxpro.domain.Quote;
import net.vokhmin.exam.fxpro.domain.Symbol;

@Slf4j
public class SimpleQuoteProducer extends AbstractQuoteProducer implements Runnable, QuoteHandler {

    private static final int SLEEP_LIMIT_MS = 10000;
    private static final int PRICE_MAX = 100;
    private static final BigDecimal PRICE_RATE = valueOf(0.5);

    private final List<Symbol> symblos = new CopyOnWriteArrayList<>();
    private final BlockingQueue<Quote> quotes;
    private final TimeService time;
    private final Random random = new Random();

    public SimpleQuoteProducer(BlockingQueue<Quote> quotes, TimeService time) {
        this.quotes = quotes;
        this.time = time;
    }

    @Override
    public void run() {
        try {
            BigDecimal prev = valueOf(random.nextDouble() * random.nextInt(PRICE_MAX));
            while (active) {
                Thread.sleep(random.nextInt(SLEEP_LIMIT_MS));
                final Quote quote = quoteOf(randomSymbol(), now(), randomPrice(prev));
                log.debug("Try to originate a new quote - {}", quote);
                quotes.put(quote);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private BigDecimal randomPrice(BigDecimal prev) {
        final BigDecimal delta = prev.multiply(PRICE_RATE);
        return random.nextBoolean()
                ? prev.add(delta)
                : prev.subtract(delta);
    }

    private long now() {
        return time.currentTimeMillis();
    }

    private Symbol randomSymbol() {
        return symblos.get(random.nextInt(symblos.size()));
    }

    // QuoteHandler implementations...

    @Override
    public void handle(Quote quote) {
        try {
            quotes.put(quote);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleBelated(Quote quote) {
        throw new UnsupportedOperationException();
    }

}
