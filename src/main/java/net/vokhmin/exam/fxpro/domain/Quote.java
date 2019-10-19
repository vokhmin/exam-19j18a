package net.vokhmin.exam.fxpro.domain;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class Quote {

    public Symbol symbol;
    public long timestamp;
    public BigDecimal price;

}
