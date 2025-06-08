package ru.demo.server;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public interface EventLoop {

    String getName();
    void start();
    void stop();

    void registerClient(SocketChannel clientSocketChannel);

    void registerServer(ServerSocketChannel serverSocketChannel);
}