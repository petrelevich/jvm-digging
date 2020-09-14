package ru.trademgr.queries;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import ru.trademgr.events.CreatedTradeEvent;
import ru.trademgr.model.Position;
import ru.trademgr.model.Side;
import ru.trademgr.model.Trade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TradesEventHandler {

    private final Map<Long, Trade> trades = new TreeMap<>();
    private final Map<String, Long> position = new HashMap<>();

    @EventHandler
    public void on(CreatedTradeEvent event) {
        log.info("event:{}", event);

        var trade = Trade.builder()
                .tradeId(event.tradeId())
                .tradeTime(event.tradeTime())
                .settlementDate(event.settlementDate())
                .name(event.name())
                .shortName(event.shortName())
                .currency(event.currency())
                .side(event.side())
                .size(event.size())
                .price(event.price())
                .amount(event.amount())
                .accruedInterest(event.accruedInterest())
                .brokerFee(event.brokerFee())
                .marketFee(event.marketFee())
                .build();
        trades.put(event.tradeId(), trade);
        position.merge(event.shortName(), event.size(),
                (oldValue, value) -> event.side() == Side.BUY ? oldValue + value : oldValue - value);
    }

    @QueryHandler
    public List<Trade> handleFindAllTradesQuery(FindAllTradesQuery query) {
        return new ArrayList<>(trades.values());
    }

    @QueryHandler
    public List<Position> handleFindCurrentPositionQuery(FindCurrentPositionQuery query) {
        if (query.getShortName() == null) {
            return position.entrySet().stream()
                    .map(entry -> Position.builder().shortName(entry.getKey()).size(entry.getValue()).build())
                    .collect(Collectors.toList());
        } else {
            Long size = position.get(query.getShortName());
            return size != null ?
                    List.of(Position.builder().shortName(query.getShortName()).size(size).build()) :
                    Collections.emptyList();
        }
    }
}
