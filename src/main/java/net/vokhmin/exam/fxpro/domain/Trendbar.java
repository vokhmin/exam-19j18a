package net.vokhmin.exam.fxpro.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
public class Trendbar {

    @Value
    @Builder
    @AllArgsConstructor
    public static class ID {
        public final TrendbarPeriod type;
        public final long timestamp;
    }

    public final ID id;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal low;
    private BigDecimal high;

}
