package net.vokhmin.exam.fxpro.service;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
public class QuoteProviders {

    private final Set<AbstractQuoteProducer> producers = ConcurrentHashMap.newKeySet();

    boolean register(AbstractQuoteProducer producer) {
        if (producers.contains(producer)) {
            log.warn("Producer {} has been registered already, ignore the second attempt", producer);
            return false;
        }
        if (producers.add(producer)) {
            log.info("Producer {} has been registered successfully", producer);
            producer.start();
            return true;
        }
        log.error("Couldn't register the Producer {}", producer)
    }

    void unregister(AbstractQuoteProducer producer) {
        if (producers.remove(producer)) {
            log.debug("Producer {} has been removed successfully", producer);
            producer.stop();
            return;
        };
        log.error("Producer {} has not been registered, it could not be found", producer);
    }

}
