package ru.trademgr.queue;

import ru.trademgr.events.CreatedTradeEvent;

import java.util.LinkedList;
import java.util.Queue;


public class EventQueueImpl implements EventQueue {
    private final Queue<CreatedTradeEvent> queue = new LinkedList<>();

    @Override
    public void putEvent(CreatedTradeEvent event) {
        queue.offer(event);
    }

    @Override
    public CreatedTradeEvent getEvent() {
        return queue.poll();
    }

    @Override
    public int size() {
        return queue.size();
    }
}
