package net.vokhmin.exam.fxpro.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuoteProviders {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Map<QuoteProducer, Future> producers = new ConcurrentHashMap();
    private final QuoteConsumer consumer;

    public QuoteProviders(QuoteConsumer consumer) {
        this.consumer = consumer;
    }

    public QuoteProducer register(QuoteProducerFactory factory) {
        final QuoteProducer producer = factory.createProducer(consumer);
        if (producers.containsKey(producer)) {
            throw new RuntimeException("Producer has been registered already");
        }
        Future<Void> future = executor.submit(producer);
        producers.put(producer, future);
        log.info("Producer {} has been registered successfully", producer);
        return producer;
    }

    public void unregister(QuoteProducer producer) {
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
