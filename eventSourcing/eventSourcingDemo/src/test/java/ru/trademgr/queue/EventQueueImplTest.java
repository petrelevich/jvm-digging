package ru.trademgr.queue;

import org.junit.jupiter.api.Test;
import ru.trademgr.events.CreatedTradeEvent;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EventQueueImplTest {

    @Test
    void queueTest() {
        //given
        var events = List.of(CreatedTradeEvent.builder().tradeId(1).build(),
                CreatedTradeEvent.builder().tradeId(2).build(),
                CreatedTradeEvent.builder().tradeId(3).build(),
                CreatedTradeEvent.builder().tradeId(4).build(),
                CreatedTradeEvent.builder().tradeId(5).build());

        //when
        var storage = new EventQueueImpl();
        for (var event : events) {
            storage.putEvent(event);
        }
        //then
        assertThat(storage.size()).isEqualTo(events.size());

        //when
        for (CreatedTradeEvent event : events) {
            assertThat(storage.getEvent()).isEqualTo(event);
        }
        //then
        assertThat(storage.size()).isZero();
    }
}