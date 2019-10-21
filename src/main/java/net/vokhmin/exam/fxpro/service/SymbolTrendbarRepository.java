package net.vokhmin.exam.fxpro.service;

import java.util.HashMap;
import java.util.Map;

import net.vokhmin.exam.fxpro.domain.Symbol;
import net.vokhmin.exam.fxpro.domain.TrendbarPeriod;

public class SymbolTrendbarRepository {

    private final Map<Symbol, TrendbarRepository> repos = new HashMap<>();

    public TrendbarRepository get(Symbol symbol, TrendbarPeriod type) {
        return repos.computeIfAbsent(symbol, key -> new TrendbarRepository());
    }

}
