package ru.trademgr.commands;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import org.axonframework.commandhandling.RoutingKey;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import ru.trademgr.model.Side;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@Accessors(fluent = true)
@Builder
public class CreateTradeCommand {
    @RoutingKey
    @TargetAggregateIdentifier
    long tradeId;

    LocalDateTime tradeTime;
    LocalDate settlementDate;
    String name;
    String shortName;
    String currency;
    Side side;
    long size;
    BigDecimal price;
    BigDecimal amount;
    BigDecimal accruedInterest;
    BigDecimal brokerFee;
    BigDecimal marketFee;
}
