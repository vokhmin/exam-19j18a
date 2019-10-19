package net.vokhmin.exam.fxpro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.NotThreadSafe;
import net.vokhmin.exam.fxpro.domain.Quote;
import net.vokhmin.exam.fxpro.domain.Trendbar;
import net.vokhmin.exam.fxpro.domain.TrendbarPeriod;
import net.vokhmin.exam.fxpro.domain.Trendbars;

@Slf4j
@NotThreadSafe
public class TbarHandler implements Runnable {

    private final BlockingQueue quotes;
    private final List<Trendbar> currents = new ArrayList<>(TrendbarPeriod.values().length);

    public TbarHandler(BlockingQueue quotesQueue) {
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
            if (currents.get(type.ordinal()) == null) {
                currents.set(
                        type.ordinal(),
                        Trendbars.newborn(type, quote.getTimestamp(), quote.getPrice())
                );
            } else {

            }
            handleByType(type, quote);
        }
        log.debug("The quote {} has been handled", quote);
    }

    public Trendbar complete(TrendbarPeriod type, long timestamp) {

    }

}
