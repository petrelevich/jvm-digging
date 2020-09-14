package ru.trademgr.events;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import ru.trademgr.model.Side;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@Builder
@Accessors(fluent = true)
public class CreatedTradeEvent {
    private long tradeId;

    private LocalDateTime tradeTime;
    private LocalDate settlementDate;
    private String name;
    private String shortName;
    private String currency;
    private Side side;
    private long size;
    private BigDecimal price;
    private BigDecimal amount;
    private BigDecimal accruedInterest;
    private BigDecimal brokerFee;
    private BigDecimal marketFee;
}
