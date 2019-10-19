package net.vokhmin.exam.fxpro.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import net.vokhmin.exam.fxpro.domain.Symbol;

@RequiredArgsConstructor
public class RandomQuotesFactory implements QuoteProducerFactory {

    private final List<Symbol> symbols;
    private final TimeService timeService;

    @Override
    public QuoteProducer createProducer(QuoteConsumer consumer) {
        return new RandomQuoteProducer(consumer, symbols, timeService);
    }

}
