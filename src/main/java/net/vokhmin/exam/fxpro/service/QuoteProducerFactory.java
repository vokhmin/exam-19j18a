package net.vokhmin.exam.fxpro.service;

public interface QuoteProducerFactory {

    QuoteProducer createProducer(QuoteConsumer consumer);

}
