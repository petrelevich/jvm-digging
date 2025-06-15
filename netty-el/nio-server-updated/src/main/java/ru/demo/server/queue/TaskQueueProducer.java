package ru.demo.server.queue;

public interface TaskQueueProducer {
    boolean offerTask(Runnable task);
}
