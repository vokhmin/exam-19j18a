package net.vokhmin.exam.fxpro.service;

import java.util.concurrent.Callable;

public interface QuoteProducerFactory {

    AbstractQuoteProducer createProducer(QuoteConsumer consumer);

}
