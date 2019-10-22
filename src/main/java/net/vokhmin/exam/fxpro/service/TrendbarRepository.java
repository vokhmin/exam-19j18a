package net.vokhmin.exam.fxpro.service;

import java.util.NavigableMap;
import java.util.Optional;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import org.springframework.data.repository.CrudRepository;

import lombok.NonNull;
import net.vokhmin.exam.fxpro.domain.Trendbar;

public class TrendbarRepository implements CrudRepository<Trendbar, Long> {

    private final NavigableMap<Long, Trendbar> data = new ConcurrentSkipListMap<>();

    public Iterable<Trendbar> findAllByTimestampBetween(@NonNull Long from, Long upTo) {
        return collect(data.subMap(from, upTo));
    }

    public Iterable<Trendbar> findAllByTimestampGreaterThanEqual(@NonNull Long from) {
        return collect(data.tailMap(from));
    }

    private Iterable<Trendbar> collect(SortedMap<Long, Trendbar> map) {
        return map.entrySet()
                .stream()
                .map(e -> e.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public <T extends Trendbar> T save(T tbar) {
        data.put(tbar.id.timestamp, tbar);
        return tbar;
    }

    @Override
    public <S extends Trendbar> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Trendbar> findById(Long timestamp) {
        return Optional.ofNullable(data.get(timestamp));
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public Iterable<Trendbar> findAll() {
        return null;
    }

    @Override
    public Iterable<Trendbar> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return data.size();
    }

    @Override
    public void deleteById(Long id) {

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
