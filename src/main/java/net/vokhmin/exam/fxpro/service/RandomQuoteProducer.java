package net.vokhmin.exam.fxpro.service;

import static java.math.BigDecimal.valueOf;

import static net.vokhmin.exam.fxpro.domain.Quote.quoteOf;

import java.util.List;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.vokhmin.exam.fxpro.domain.Quote;
import net.vokhmin.exam.fxpro.domain.Symbol;

@Slf4j
@RequiredArgsConstructor
public class RandomQuoteProducer implements QuoteProducer {

    private final QuoteConsumer consumer;
    private final List<Symbol> symbols;
    private final TimeService timeService;
    private final Random random = new Random();

    @Override
    public Void call() {
        try {
            while (true) {
                Thread.sleep(random.nextInt(200));
                final Quote quote = quoteOf(
                        symbols.get(symbols.size() > 1 ? random.nextInt(symbols.size() - 1) : 0),
                        timeService.currentTimeMillis(),
                        valueOf(random.nextDouble())
                );
                log.debug("Trying to feed a new quote {}", quote);
                consumer.accept(quote);
            }
        } catch (Exception e) {
            log.error("has got an exception {}", e.getMessage(), e);
            return null;
        }
    }
}
