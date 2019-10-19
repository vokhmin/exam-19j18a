package net.vokhmin.exam.fxpro.service;

public class CustomTimeService implements TimeService {

    private volatile long currentTimeMillis;

    public void setCurrentTimeMillis(long ms) {
        currentTimeMillis = ms;
    }

    @Override
    public long currentTimeMillis() {
        return currentTimeMillis;
    }

}
