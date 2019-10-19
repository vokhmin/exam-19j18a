package net.vokhmin.exam.fxpro.domain;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class Quote {

    Symbol symbol;
    long timestamp;
    BigDecimal price;

}
