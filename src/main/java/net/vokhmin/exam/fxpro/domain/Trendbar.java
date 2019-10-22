package net.vokhmin.exam.fxpro.domain;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
public class Trendbar {

    @Value
    @Builder
    public static class ID {
        public final TrendbarPeriod type;
        public final long timestamp;
    }

    public final ID id;
    BigDecimal open;
    BigDecimal close;
    BigDecimal low;
    BigDecimal high;

}
