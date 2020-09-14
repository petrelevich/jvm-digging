package ru.trademgr.gui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.trademgr.model.Position;
import ru.trademgr.model.Trade;
import ru.trademgr.queries.FindAllTradesQuery;
import ru.trademgr.queries.FindCurrentPositionQuery;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TradeController {

    private final QueryGateway queryGateway;

    @GetMapping("/trade/all")
    public List<Trade> findAllTrades() {
        return queryGateway.query(new FindAllTradesQuery(),
                ResponseTypes.multipleInstancesOf(Trade.class)).join();
    }

    @GetMapping("/currentPosition/{shortName}")
    public List<Position> findCurrentPosition(@PathVariable("shortName") String shortName) {
        return queryGateway.query(new FindCurrentPositionQuery(shortName),
                ResponseTypes.multipleInstancesOf(Position.class)).join();
    }

    @GetMapping("/currentPosition")
    public List<Position> findCurrentPosition() {
        return queryGateway.query(new FindCurrentPositionQuery(null),
                ResponseTypes.multipleInstancesOf(Position.class)).join();
    }
}
