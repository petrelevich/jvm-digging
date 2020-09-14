package ru.trademgr.aggregates;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import ru.trademgr.commands.CreateTradeCommand;
import ru.trademgr.events.CreatedTradeEvent;

import java.util.UUID;

@Aggregate
@Slf4j
@NoArgsConstructor
public class TradeAggregate {

    @AggregateIdentifier
    private UUID id = UUID.randomUUID();

    @CommandHandler
    public TradeAggregate(CreateTradeCommand command) {
        log.info("command: {}", command);
        var event = CreatedTradeEvent.builder()
                .tradeId(command.tradeId())
                .tradeTime(command.tradeTime())
                .settlementDate(command.settlementDate())
                .name(command.name())
                .shortName(command.shortName())
                .currency(command.currency())
                .side(command.side())
                .size(command.size())
                .price(command.price())
                .amount(command.amount())
                .accruedInterest(command.accruedInterest())
                .brokerFee(command.brokerFee())
                .marketFee(command.marketFee())
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(CreatedTradeEvent event) {
        log.info("on event: {}", event);
    }
}
