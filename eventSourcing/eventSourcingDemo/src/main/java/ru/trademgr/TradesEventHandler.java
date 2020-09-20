package ru.trademgr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.trademgr.events.CreatedTradeEvent;
import ru.trademgr.model.Position;
import ru.trademgr.model.Side;
import ru.trademgr.model.Trade;
import ru.trademgr.queue.EventQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TradesEventHandler {
    private static final Logger log = LoggerFactory.getLogger(TradesEventHandler.class);

    private final EventQueue eventQueue;
    private final Map<Long, Trade> trades = new ConcurrentHashMap<>();
    private final Map<String, Long> position = new ConcurrentHashMap<>();

    public TradesEventHandler(EventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void onEvent(CreatedTradeEvent event) {
        log.info("event:{}", event);
        eventQueue.putEvent(event);
        var trade = Trade.builder()
                .tradeId(event.getTradeId())
                .shortName(event.getShortName())
                .side(event.getSide())
                .size(event.getSize())
                .price(event.getPrice())
                .build();
        trades.put(event.getTradeId(), trade);
        position.merge(event.getShortName(), event.getSize(),
                (oldValue, value) -> event.getSide() == Side.BUY ? oldValue + value : oldValue - value);
    }

    public List<Trade> getAllTrades() {
        return new ArrayList<>(trades.values());
    }

    public List<Position> getCurrentPosition() {
        return position.entrySet().stream()
                .map(entry -> Position.builder().shortName(entry.getKey()).size(entry.getValue()).build())
                .collect(Collectors.toList());
    }
}
