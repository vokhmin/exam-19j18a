package net.vokhmin.exam.fxpro.service;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class QuoteProviders {

    private final Set<AbstractQuoteProducer> producers = new ConcurrentSkipListSet<>();

    boolean register(AbstractQuoteProducer producer) {
        if (producers.contains(producer)) {
            return false;
        }
        producers.add(producer);
        producer.start();
        return true;
    }

    void unregister(AbstractQuoteProducer producer) {

    }

}
