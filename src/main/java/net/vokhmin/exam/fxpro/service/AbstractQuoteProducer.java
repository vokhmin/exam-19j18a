package net.vokhmin.exam.fxpro.service;

public abstract class AbstractQuoteProducer implements QuoteProducer {

    private volatile boolean active;

    public void start() {
        active = true;
    }

    public void stop() {
        active = false;
    }

    protected boolean isRunning() {
        return active;
    }

}
