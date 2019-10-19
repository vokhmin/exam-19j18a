package net.vokhmin.exam.fxpro.service;

import java.util.concurrent.BlockingQueue;

import lombok.extern.slf4j.Slf4j;
import net.vokhmin.exam.fxpro.domain.Quote;

@Slf4j
public class QuoteHandlerWorker implements Runnable {

    private final BlockingQueue<Quote> quotes;
    private final QuoteHandler handler;

    public QuoteHandlerWorker(
            BlockingQueue<Quote> queue,
            QuoteHandler handler
    ) {
        this.quotes = queue;
        this.handler = handler;
    }

    public void run() {
        try {
            while (true) {
                final Object quote = quotes.take();
                assert quote instanceof Quote;
                log.debug("Have got the {} for handling", quote);
                handler.handle((Quote) quote);
                log.debug("The {} was handled successfully", quote);
            }
        } catch (InterruptedException e) {
            log.error("Unexpected error", e);
        }
    }

}
