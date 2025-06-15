package ru.demo.server.queue;

public interface TaskQueueConsumer {
   Runnable pollTask();
}
