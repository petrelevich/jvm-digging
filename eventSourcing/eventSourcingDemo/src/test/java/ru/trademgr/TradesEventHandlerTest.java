package ru.trademgr;

import org.junit.jupiter.api.Test;
import ru.trademgr.events.CreatedTradeEvent;
import ru.trademgr.model.Position;
import ru.trademgr.model.Side;
import ru.trademgr.model.Trade;
import ru.trademgr.queue.EventQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class TradesEventHandlerTest {

    @Test
    void testPosition() {
        //given
        var eventQueue = mock(EventQueue.class);
        var tradesEventHandler = new TradesEventHandler(eventQueue);

        var event1 = CreatedTradeEvent.builder()
                .tradeId(1)
                .size(10)
                .side(Side.BUY)
                .shortName("SBERP")
                .build();

        var event2 = CreatedTradeEvent.builder()
                .tradeId(2)
                .size(100)
                .side(Side.BUY)
                .shortName("SIBN")
                .build();

        var event3 = CreatedTradeEvent.builder()
                .tradeId(3)
                .size(1)
                .side(Side.SELL)
                .shortName("SBERP")
                .build();
        //when
        tradesEventHandler.onEvent(event1);
        tradesEventHandler.onEvent(event2);
        tradesEventHandler.onEvent(event3);
        var allTrades = tradesEventHandler.getAllTrades();
        var position = tradesEventHandler.getCurrentPosition();

        //then
        verify(eventQueue).putEvent(event1);
        verify(eventQueue).putEvent(event2);
        verify(eventQueue).putEvent(event3);

        assertThat(allTrades.size()).isEqualTo(3);
        assertThat(allTrades).contains(
                Trade.builder().tradeId(event1.getTradeId()).size(event1.getSize()).side(event1.getSide()).shortName(event1.getShortName()).build(),
                Trade.builder().tradeId(event2.getTradeId()).size(event2.getSize()).side(event2.getSide()).shortName(event2.getShortName()).build(),
                Trade.builder().tradeId(event3.getTradeId()).size(event3.getSize()).side(event3.getSide()).shortName(event3.getShortName()).build());

        assertThat(position.size()).isEqualTo(2);
        assertThat(position).contains(Position.builder().shortName("SBERP").size(9).build(),
                Position.builder().shortName("SIBN").size(100).build());
    }
}