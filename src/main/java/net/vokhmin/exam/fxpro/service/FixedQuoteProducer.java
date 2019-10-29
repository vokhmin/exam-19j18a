package net.vokhmin.exam.fxpro.service;

import static net.vokhmin.exam.fxpro.domain.Quote.quoteOf;

import java.util.Queue;

import lombok.RequiredArgsConstructor;
import net.vokhmin.exam.fxpro.domain.Quote;

@RequiredArgsConstructor
public class FixedQuoteProducer implements QuoteProducer {

    private final QuoteConsumer consumer;
    private final Queue<Quote> quotes;
    private final TimeService timeService;

    @Override
    public Void call() throws InterruptedException {
        final long startAt = timeService.currentTimeMillis();
        Quote quote;
        while ((quote = quotes.poll()) != null) {
            Thread.sleep(quote.timestamp);
            consumer.accept(quoteOf(
                    quote.symbol,
                    quote.timestamp + startAt,
                    quote.price)
            );
        }
        return null;
    }
}
