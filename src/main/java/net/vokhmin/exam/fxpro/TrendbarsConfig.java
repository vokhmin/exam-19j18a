package net.vokhmin.exam.fxpro;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.vokhmin.exam.fxpro.domain.Quote;
import net.vokhmin.exam.fxpro.domain.QuotePriorityComparator;
import net.vokhmin.exam.fxpro.service.QuoteHandler;
import net.vokhmin.exam.fxpro.service.QuoteHandlerWorker;
import net.vokhmin.exam.fxpro.service.QuoteService;
import net.vokhmin.exam.fxpro.service.TrendbarService;
import net.vokhmin.exam.fxpro.service.TrendbarStorage;

@Configuration
public class TrendbarsConfig {

    private static final int QUOTES_QUEUE_CAPACITY = 1_000;

    @Bean
    BlockingQueue<Quote> quotesQueue() {
        return new PriorityBlockingQueue(QUOTES_QUEUE_CAPACITY, new QuotePriorityComparator());
    }

    @Bean
    TrendbarStorage trendbarStorage() {
        return new TrendbarStorage();
    }

    @Bean
    TrendbarService trendbarService(
            BlockingQueue<Quote> queue
    ) {
        return new TrendbarService(queue);
    }

    @Bean
    QuoteHandler quoteHandler(TrendbarStorage storage) {
        return new QuoteService(storage);
    }

    @Bean
    QuoteHandlerWorker quoteHandlerWorker(
            BlockingQueue<Quote> queue,
            QuoteHandler handler
    ) {
        return new QuoteHandlerWorker(queue, handler);
    }
}
