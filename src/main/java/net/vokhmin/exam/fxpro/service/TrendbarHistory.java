package net.vokhmin.exam.fxpro.service;

import java.util.List;

import lombok.NonNull;
import net.vokhmin.exam.fxpro.domain.Symbol;
import net.vokhmin.exam.fxpro.domain.Trendbar;
import net.vokhmin.exam.fxpro.domain.TrendbarPeriod;

public interface TrendbarHistory {

    List<Trendbar> getSeries(
            @NonNull Symbol symbol,
            @NonNull TrendbarPeriod type,
            @NonNull Long from,
            Long upTo
    );

}
