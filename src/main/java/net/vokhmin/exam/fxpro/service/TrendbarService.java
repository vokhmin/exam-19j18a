package net.vokhmin.exam.fxpro.service;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.NonNull;
import net.vokhmin.exam.fxpro.domain.Symbol;
import net.vokhmin.exam.fxpro.domain.Trendbar;
import net.vokhmin.exam.fxpro.domain.TrendbarPeriod;

public class TrendbarService implements HistoryService {

    @Autowired
    private TrendbarSeries series;

    @Override
    public List<Trendbar> getSeries(
            @NonNull Symbol symbol,
            @NonNull TrendbarPeriod type,
            @NonNull Long from,
            Long upTo
    ) {
        final Iterable<Trendbar> iter = nonNull(upTo)
                ? series.get(symbol, type).findAllByTimestampBetween(from, upTo)
                : series.get(symbol, type).findAllByTimestampGreaterThanEqual(from);
        return StreamSupport.stream(iter.spliterator(), false)
                .collect(Collectors.toUnmodifiableList());
    }

    public void setSeries(TrendbarSeries series) {
        this.series = series;
    }
}
