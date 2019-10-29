package net.vokhmin.exam.fxpro.service;

import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(it -> save(it))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Trendbar> findById(Long timestamp) {
        return Optional.ofNullable(data.get(timestamp));
    }

    @Override
    public boolean existsById(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<Trendbar> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Iterable<Trendbar> findAllById(Iterable<Long> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        return data.size();
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Trendbar trendbar) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends Trendbar> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }
}
