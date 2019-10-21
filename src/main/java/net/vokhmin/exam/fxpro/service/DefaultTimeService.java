package net.vokhmin.exam.fxpro.service;

public class DefaultTimeService implements TimeService {

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

}
