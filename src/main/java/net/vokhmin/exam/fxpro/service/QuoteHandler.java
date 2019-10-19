package net.vokhmin.exam.fxpro.service;

import net.vokhmin.exam.fxpro.domain.Quote;

public interface QuoteHandler {

    void handle(Quote quote);

    void handleBelated(Quote quote);

}
