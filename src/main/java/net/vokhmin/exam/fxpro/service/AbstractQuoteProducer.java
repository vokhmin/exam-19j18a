package net.vokhmin.exam.fxpro.service;

public abstract class AbstractQuoteProducer {

    private volatile boolean active;
    private final QuoteConsumer consumer;

    protected AbstractQuoteProducer(QuoteConsumer consumer) {
        this.consumer = consumer;
    }

    public void start() {
        Thread thread = newThread();
        active = true;
        thread.start();
    }

    public void stop() {
        active = false;
    }

    ;

    protected abstract Thread newThread();

    protected boolean isRunning() {
        return active;
    }

}
