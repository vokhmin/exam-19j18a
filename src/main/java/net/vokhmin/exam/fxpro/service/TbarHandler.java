package net.vokhmin.exam.fxpro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.NotThreadSafe;
import net.vokhmin.exam.fxpro.domain.Quote;
import net.vokhmin.exam.fxpro.domain.Trendbar;
import net.vokhmin.exam.fxpro.domain.TrendbarPeriod;
import net.vokhmin.exam.fxpro.domain.Trendbars;

@Slf4j
@NotThreadSafe
public class TbarHandler implements Runnable {

    private final PriorityBlockingQueue<Quote> quotes;
    private final List<Trendbar> currents = new ArrayList<>(TrendbarPeriod.values().length);

    public TbarHandler(PriorityBlockingQueue<Quote> quotesQueue) {
        this.quotes = quotesQueue;
    }

    public void run() {
        try {
            final Object quote = quotes.take();
            assert quote instanceof Quote;
            handle((Quote) quote);
        } catch (InterruptedException e) {
            log.error("Unexpected error", e);
        }
    }

    public void handle(Quote quote) {
        log.debug("Try to handle a quote {}", quote);
        for (final TrendbarPeriod type : TrendbarPeriod.values()) {
            final Trendbar current = currents.get(type.ordinal());
            if (current == null) {
                currents.set(
                        type.ordinal(),
                        Trendbars.firstborn(type, quote.getTimestamp(), quote.getPrice())
                );
            } else {
                switch (
                        Long.compare(
                                current.id.timestamp,
                                Trendbars.bornTime(type, quote.timestamp))
                ) {
                    case -1:    // the next trendbar generation
                        currents.set(
                                type.ordinal(),
                                Trendbars.offspring(current, quote)
                        );
//                        complete(current);
                    case 0:     // the same trendbar generation
                        currents.set(
                                type.ordinal(),
                                Trendbars.offspring(current, quote)
                        );
                    case 1:    // unexpectedly a belated quote! the previous trendbar generation
                        handleBelatedQuote();
                }
            }
        }
        log.debug("The quote {} has been handled", quote);
    }

    private void handleBelatedQuote() {
        log.warn("The belated message has been got! It will be ignored and dropped.");
    }

    public Trendbar complete(TrendbarPeriod type, long timestamp) {
        return null;
    }

}
