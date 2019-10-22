package net.vokhmin.exam.fxpro.domain;

import static net.vokhmin.exam.fxpro.RandomUtils.nextBigDecimal;
import static net.vokhmin.exam.fxpro.RandomUtils.nextLong;

public class TestedQuotes {

    public static Quote randomQuote(Symbol symbol) {
        return new Quote(symbol, nextLong(), nextBigDecimal());
    }

}
