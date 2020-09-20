package ru.trademgr.storage;

import ru.trademgr.events.CreatedTradeEvent;

public interface Serializer {
    String serializeEvent(CreatedTradeEvent event);
    CreatedTradeEvent deserializeEvent(String eventAsString);
}
