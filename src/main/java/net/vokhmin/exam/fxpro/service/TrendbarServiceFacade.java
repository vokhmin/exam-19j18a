package net.vokhmin.exam.fxpro.service;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.vokhmin.exam.fxpro.domain.Quote;
import net.vokhmin.exam.fxpro.domain.Symbol;
import net.vokhmin.exam.fxpro.domain.Trendbar;
import net.vokhmin.exam.fxpro.domain.TrendbarPeriod;

@Slf4j
@RequiredArgsConstructor
public class TrendbarServiceFacade implements QuoteConsumer, TrendbarHistory {

    private static final long DELIVERY_TIMEOUT = 100;

    private final BlockingQueue<Quote> quotes;

    private TrendbarStorage series;

    @Autowired
    public void setSeries(TrendbarStorage series) {
        this.series = series;
    }

    /**
     * Handler for quotes that couldn't be delivered in a reasonable time.
     */
    protected void onDeliveryTimeout(Quote quote) {
        log.error("The quote {} can't be put in quotes' queue in time, so it'll be dropped", quote);
    }

    // HistoryService implementation ...

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

    // QuoteConsumer implementations ...

    @Override
    public void accept(Quote quote) {
        try {
            if (!quotes.offer(quote, DELIVERY_TIMEOUT, TimeUnit.MILLISECONDS)) {
                onDeliveryTimeout(quote);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
