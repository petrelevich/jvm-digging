package ru.demo.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface ClientRequestHandle {
    void handle(SocketChannel clientSocketChannel) throws IOException;
}
