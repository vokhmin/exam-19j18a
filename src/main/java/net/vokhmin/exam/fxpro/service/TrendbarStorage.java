package net.vokhmin.exam.fxpro.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.vokhmin.exam.fxpro.domain.Symbol;
import net.vokhmin.exam.fxpro.domain.TrendbarPeriod;

public class TrendbarStorage {

    private final Map<Symbol, List<TrendbarRepository>> repos = new ConcurrentHashMap<>();

    public TrendbarRepository get(Symbol symbol, TrendbarPeriod type) {
        final List<TrendbarRepository> series = repos.computeIfAbsent(symbol, key -> createSeries());
        return series.get(type.ordinal());
    }

    private List<TrendbarRepository> createSeries() {
        final ArrayList<TrendbarRepository> data = new ArrayList<>(TrendbarPeriod.values().length);
        Arrays.stream(TrendbarPeriod.values()).forEach(it -> data.add(new TrendbarRepository()));
        return data;
    }

}
