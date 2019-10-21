package net.vokhmin.exam.fxpro.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.data.repository.CrudRepository;

import net.vokhmin.exam.fxpro.domain.Trendbar;
import net.vokhmin.exam.fxpro.domain.TrendbarPeriod;

public class TrendbarRepository implements CrudRepository<Trendbar, Trendbar.ID> {

    private final List<NavigableMap<Long, Trendbar>> data;

    public TrendbarRepository() {
        data = new ArrayList<>(TrendbarPeriod.values().length);
        Collections.fill(data, new ConcurrentSkipListMap<>());
    }

    @Override
    public <T extends Trendbar> T save(T tbar) {
        series(tbar).put(tbar.id.timestamp, tbar);
        return tbar;
    }

    private <T extends Trendbar> NavigableMap<Long, Trendbar> series(T tbar) {
        return data.get(tbar.id.type.ordinal());
    }

    @Override
    public <S extends Trendbar> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Trendbar> findById(Trendbar.ID id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Trendbar.ID id) {
        return false;
    }

    @Override
    public Iterable<Trendbar> findAll() {
        return null;
    }

    @Override
    public Iterable<Trendbar> findAllById(Iterable<Trendbar.ID> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Trendbar.ID id) {

    }

    @Override
    public void delete(Trendbar trendbar) {

    }

    @Override
    public void deleteAll(Iterable<? extends Trendbar> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
