package net.vokhmin.exam.fxpro.service;

public abstract class AbstractQuoteProducer {

    private volatile boolean active;

    public void start() {
        Thread thread = newThread();
        active = true;
        thread.start();
    }

    public  void stop() {
        active = false;
    };

    protected abstract Thread newThread();

}
