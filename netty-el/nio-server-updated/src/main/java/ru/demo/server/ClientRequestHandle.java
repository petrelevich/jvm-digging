package ru.demo.server;

import ru.demo.server.queue.TaskQueueProducer;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface ClientRequestHandle {
    void handle(TaskQueueProducer taskQueueProducer, SocketChannel clientSocketChannel) throws IOException;
}
