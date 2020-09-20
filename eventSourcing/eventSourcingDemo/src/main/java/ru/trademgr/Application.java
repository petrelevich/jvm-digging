package ru.trademgr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.trademgr.events.CreatedTradeEvent;
import ru.trademgr.model.Side;
import ru.trademgr.queue.EventQueueImpl;
import ru.trademgr.storage.Serializer;
import ru.trademgr.storage.SerializerJson;
import ru.trademgr.storage.Storage;
import ru.trademgr.storage.StorageFile;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private final EventQueueImpl queue = new EventQueueImpl();
    private final TradesEventHandler tradesEventHandler = new TradesEventHandler(queue);

    private final Serializer serializer = new SerializerJson();
    private final Storage storage = new StorageFile(Paths.get("tradeQueue.dat"));

    public static void main(String[] args) {
        var application = new Application();
        application.restore();

        var event1 = CreatedTradeEvent.builder()
                .tradeId(1).size(10).side(Side.BUY).shortName("SBERP").price(new BigDecimal("220.12")).build();
        var event2 = CreatedTradeEvent.builder()
                .tradeId(2).size(100).side(Side.BUY).shortName("SIBN").price(new BigDecimal("304")).build();

        var event3 = CreatedTradeEvent.builder()
                .tradeId(3).size(1).side(Side.SELL).shortName("SBERP").price(new BigDecimal("222.14")).build();

        application.processEvent(event1);
        application.processEvent(event2);
        application.processEvent(event3);

        application.printState();
    }

    private void printState() {
        var position = tradesEventHandler.getCurrentPosition();
        log.info("current position:{}", position);
    }

    private void restore() {
        var events = storage.loadEvents().stream()
                .map(serializer::deserializeEvent)
                .collect(Collectors.toList());
        log.info("loaded events:{}", events.size());

        for (var event: events) {
            tradesEventHandler.onEvent(event);
        }
    }

    private void processEvent(CreatedTradeEvent event) {
        var eventAsString = serializer.serializeEvent(event);
        storage.saveEvent(eventAsString);
        tradesEventHandler.onEvent(event);
    }
}
