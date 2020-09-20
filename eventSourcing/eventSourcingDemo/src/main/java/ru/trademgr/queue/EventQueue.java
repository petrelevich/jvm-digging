package ru.trademgr.queue;

import ru.trademgr.events.CreatedTradeEvent;

public interface EventQueue {
    void putEvent(CreatedTradeEvent event);

    CreatedTradeEvent getEvent();

    int size();
}
