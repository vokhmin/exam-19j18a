package net.vokhmin.exam.fxpro.service;

import java.util.Map;
import java.util.concurrent.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class QuoteProviders {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Map<QuoteProducer, Future> producers = new ConcurrentHashMap();
    private final QuoteConsumer consumer;

    QuoteProducer register(QuoteProducerFactory factory) {
        final QuoteProducer producer = factory.createProducer(consumer);
        if (producers.containsKey(producer)) {
            throw new RuntimeException("Producer has been registered already");
        }
        Future<Void> future = executor.submit(producer);
        producers.put(producer, future);
        log.info("Producer {} has been registered successfully", producer);
        return producer;
    }

    void unregister(AbstractQuoteProducer producer) {
        final Future future;
        synchronized (producer) {
            future = producers.remove(producer);
            if (future == null) {
                throw new RuntimeException("An unknown producer");
            }
            log.debug("Producer {} has been removed successfully", producer);
        }
        if (future.cancel(true)) {
            log.debug("Producer's task has been cancelled successfully");
        }
        log.error("Producer {} has not been registered, it could not be found", producer);
    }

}
