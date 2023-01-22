package ru.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerNIO {
    private static final Logger logger = LoggerFactory.getLogger(ServerNIO.class);

    private static final int PORT_0 = 8090;

    private final Executor blockingThreadExecutor = Executors.newFixedThreadPool(10,
            new ThreadFactory() {
                private final AtomicLong threadIdGenerator = new AtomicLong(0);

                @Override
                public Thread newThread(@NotNull Runnable task) {
                    var thread = new Thread(task);
                    thread.setName("blocking-thread-" + threadIdGenerator.incrementAndGet());
                    return thread;
                }
            });

    public static void main(String[] args) throws IOException {
        new ServerNIO().go();
    }

    private void go() throws IOException {
        try (var serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT_0));

            try (var selector = Selector.open()) {
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                while (!Thread.currentThread().isInterrupted()) {
                    logger.info("waiting for client");
                    selector.select(this::performIO);
                }
            }
        }
    }

    private void performIO(SelectionKey selectedKey) {
        try {
            logger.info("something happened, key:{}", selectedKey);
            if (selectedKey.isAcceptable()) {
                acceptConnection(selectedKey);
            } else if (selectedKey.isReadable()) {
                readWriteClient((SocketChannel) selectedKey.channel());
            }
        } catch (Exception ex) {
            throw new NetworkException(ex);
        }
    }

    private void acceptConnection(SelectionKey key) throws IOException {
        Selector selector = key.selector();
        logger.info("accept client connection, key:{}, selector:{}", key, selector);
        // selector=sun.nio.ch.EPollSelectorImpl - Linux epoll based Selector implementation
        var serverSocketChannel = (ServerSocketChannel) key.channel();
        var socketChannel = serverSocketChannel.accept(); //The socket channel for the new connection

        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        logger.info("socketChannel:{}", socketChannel);
    }

    private void readWriteClient(SocketChannel socketChannel) throws IOException {
        logger.info("read from client");
        try {
            var requestFromClient = handleRequest(socketChannel);
            if ("stop".equals(requestFromClient)) {
                socketChannel.close();
            } else {

                blockingThreadExecutor.execute(() -> {
                    var responseForClient = processClientRequest(requestFromClient);
                    sendResponse(socketChannel, responseForClient);
                });
            }
        } catch (Exception ex) {
            logger.error("error sending response", ex);
            socketChannel.close();
        }
    }

    private String handleRequest(SocketChannel socketChannel) throws IOException {
        var buffer = ByteBuffer.allocate(5);
        var inputBuffer = new StringBuilder(100);

        while (socketChannel.read(buffer) > 0) {
            buffer.flip();
            var input = StandardCharsets.UTF_8.decode(buffer).toString();
            logger.info("from client: {} ", input);

            buffer.flip();
            inputBuffer.append(input);
        }

        String requestFromClient = inputBuffer.toString().replace("\n", "").replace("\r", "");
        logger.info("requestFromClient: {} ", requestFromClient);
        return requestFromClient;
    }

    private void sendResponse(SocketChannel socketChannel, String responseForClient) {
        try {
            var buffer = ByteBuffer.allocate(5);
            var response = responseForClient.getBytes();
            for (byte b : response) {
                buffer.put(b);
                if (buffer.position() == buffer.limit()) {
                    buffer.flip();
                    socketChannel.write(buffer);
                    buffer.flip();
                }
            }
            if (buffer.hasRemaining()) {
                buffer.flip();
                socketChannel.write(buffer);
            }
        } catch (Exception ex) {
            logger.error("error:{} ", ex.getMessage(), ex);
        }
    }

    private String processClientRequest(String input) {
        logger.info("waiting...");
        sleep();
        return String.format("echo: %s%n", input);
    }

    private void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
