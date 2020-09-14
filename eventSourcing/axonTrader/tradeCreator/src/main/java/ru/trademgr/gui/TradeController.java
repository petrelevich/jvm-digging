package ru.trademgr.gui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.trademgr.commands.CreateTradeCommand;
import ru.trademgr.model.Trade;

import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TradeController {

    private final CommandGateway commandGateway;

    @PostMapping("/trade")
    public ResponseEntity<String> create(@RequestBody Trade trade) {
        log.info("trade:{}", trade);
        var createTradeCommand = CreateTradeCommand.builder()
                .tradeId(trade.getTradeId())
                .tradeTime(trade.getTradeTime())
                .settlementDate(trade.getSettlementDate())
                .name(trade.getName())
                .shortName(trade.getShortName())
                .currency(trade.getCurrency())
                .side(trade.getSide())
                .size(trade.getSize())
                .price(trade.getPrice())
                .amount(trade.getAmount())
                .accruedInterest(trade.getAccruedInterest())
                .brokerFee(trade.getBrokerFee())
                .marketFee(trade.getMarketFee())
                .build();
        try {
            var result = commandGateway.sendAndWait(createTradeCommand, 3, TimeUnit.SECONDS);
            log.info(result.toString());
            return ResponseEntity.ok(result.toString());
        } catch (Exception ex) {
            log.error("error while processing trade:{}", trade, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
