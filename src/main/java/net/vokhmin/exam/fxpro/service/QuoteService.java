package net.vokhmin.exam.fxpro.service;

import lombok.extern.slf4j.Slf4j;
import net.vokhmin.exam.fxpro.domain.Quote;
import net.vokhmin.exam.fxpro.domain.Symbol;
import net.vokhmin.exam.fxpro.domain.Trendbar;
import net.vokhmin.exam.fxpro.domain.TrendbarPeriod;
import net.vokhmin.exam.fxpro.domain.Trendbars;

@Slf4j
public class QuoteService implements QuoteHandler {

    private final TrendbarStorage storage;
    private final Trendbar[] currents = new Trendbar[TrendbarPeriod.values().length];

    public QuoteService(TrendbarStorage storage) {
        this.storage = storage;
    }

    private Trendbar onComplete(Symbol symbol, Trendbar bar) {
        log.debug("A new trendbar for Symbol:{} is completed - {}", symbol, bar);
        return storage.get(symbol, bar.id.type).save(bar);
    }

    public void handle(Quote... quotes) {
        for (Quote it : quotes) {
            handle(it);
        }
    }

    // QuoteHandler implementation ...

    @Override
    public void handle(Quote quote) {
        log.debug("Try to handle a quote {}", quote);
        for (final TrendbarPeriod type : TrendbarPeriod.values()) {
            final Trendbar current = currents[type.ordinal()];
            final long origin = Trendbars.bornTime(type, quote.timestamp);
            if (current == null) {
                currents[type.ordinal()] = Trendbars.firstborn(type, origin, quote.getPrice());
            } else {
                switch (Long.compare(current.id.timestamp, origin)) {
                    case -1:    // the next trendbar generation
                        onComplete(quote.symbol, current);
                        currents[type.ordinal()] = Trendbars.newborn(current, quote);
                        break;
                    case 0:     // the same trendbar generation
                        currents[type.ordinal()] = Trendbars.mutant(current, quote);
                        break;
                    case 1:    // unexpectedly a belated quote! the previous trendbar generation
                        handleBelated(quote);
                        break;
                }
            }
        }
        log.debug("The quote {} has been handled", quote);
    }

    @Override
    public void handleBelated(Quote quote) {
        log.warn("The belated quote has been got! It will be dropped - {}", quote);
    }

}
