package net.vokhmin.exam.fxpro.service;

import java.util.concurrent.BlockingQueue;

import lombok.extern.slf4j.Slf4j;
import net.vokhmin.exam.fxpro.domain.Quote;

@Slf4j
public class QuoteConsumer implements Runnable {

    private final BlockingQueue<Quote> quotes;
    private final QuoteHandler handler;

    public QuoteConsumer(
            BlockingQueue<Quote> queue,
            QuoteHandler handler
    ) {
        this.quotes = queue;
        this.handler = handler;
    }

    public void run() {
        try {
            final Object quote = quotes.take();
            assert quote instanceof Quote;
            log.debug("Have got the {} for handling", quote);
            handler.handle((Quote) quote);
            log.debug("The {} was handled successfully", quote);
        } catch (InterruptedException e) {
            log.error("Unexpected error", e);
        }
    }

}
