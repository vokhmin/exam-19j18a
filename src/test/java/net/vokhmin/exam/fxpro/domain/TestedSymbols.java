package net.vokhmin.exam.fxpro.domain;

import static net.vokhmin.exam.fxpro.RandomUtils.randomAlphabetic;

public class TestedSymbols {

    public static Symbol randomSymbol() {
        return new Symbol(randomAlphabetic(6));
    }

}
