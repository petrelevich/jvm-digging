package ru.demo.server;

import java.nio.channels.SocketChannel;
import java.util.function.Consumer;

public interface ClientConnectionHandler extends Consumer<SocketChannel> {
}
