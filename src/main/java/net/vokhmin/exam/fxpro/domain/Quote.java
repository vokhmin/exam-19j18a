package net.vokhmin.exam.fxpro.domain;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class Quote {

    public Symbol symbol;
    public long timestamp;
    public BigDecimal price;

    public static Quote quoteOf(Symbol symbol, long timestamp, BigDecimal price) {
        return new Quote(symbol, timestamp, price);
    }

}
