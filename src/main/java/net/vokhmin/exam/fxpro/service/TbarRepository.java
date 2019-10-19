package net.vokhmin.exam.fxpro.service;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import net.vokhmin.exam.fxpro.domain.Trendbar;
import net.vokhmin.exam.fxpro.domain.TrendbarPeriod;

public class TbarRepository implements CrudRepository<Trendbar, Trendbar.ID> {

    private final List<Map<Long, TrendbarPeriod>> periods = unmodifiableList(new ArrayList<>(TrendbarPeriod.values().length));

    @Override
    public <T extends Trendbar> T save(T tbar) {
        periods.get(tbar.getId().getType().ordinal()).computeIfPresent()
        return null;
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
