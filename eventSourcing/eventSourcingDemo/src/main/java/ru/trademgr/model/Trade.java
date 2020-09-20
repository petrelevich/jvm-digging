package ru.trademgr.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Trade {
    long tradeId;
    String shortName;
    Side side;
    long size;
    BigDecimal price;
}
