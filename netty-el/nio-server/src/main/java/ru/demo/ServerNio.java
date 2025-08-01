package ru.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;

public class ServerNio {
    private static final Logger logger = LoggerFactory.getLogger(ServerNio.class);

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        new ServerNio().go();
    }

    private void go() throws IOException {
        try (var serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));

            try (var selector = Selector.open()) {
                serverSocketChannel.register(selector, OP_ACCEPT);

                logger.info("waiting for client");
                while (!Thread.currentThread().isInterrupted()) {
                    selector.selectNow(this::performIO);
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
                readWriteClient(selectedKey);
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
        var socketChannel = serverSocketChannel.accept(); // The socket channel for the new connection

        socketChannel.configureBlocking(false);
        socketChannel.register(selector, OP_READ);
        logger.info("socketChannel:{}", socketChannel);
    }

    private void readWriteClient(SelectionKey selectionKey) throws IOException {
        logger.info("read from client");
        var socketChannel = (SocketChannel) selectionKey.channel();

        try {
            var requestFromClient = handleRequest(socketChannel);
            if (requestFromClient != null) {
                var responseForClient = processClientRequest(requestFromClient);
                sendResponse(socketChannel, responseForClient);
            } else {
                socketChannel.close();
            }
        } catch (Exception ex) {
            if (ex instanceof IOException && "Broken pipe".equals(ex.getMessage())) {
                logger.error("Client disconnected");
            } else {
                logger.error("error sending response", ex);
            }
            socketChannel.close();
        }
    }

    private String handleRequest(SocketChannel socketChannel) throws IOException {
        var buffer = ByteBuffer.allocate(20);
        var inputBuffer = new StringBuilder(100);

        var reqestSize = 0;
        var readBytes = 0;
        while ( (readBytes = socketChannel.read(buffer)) > 0) {
            reqestSize += readBytes;
            buffer.flip();
            var input = StandardCharsets.UTF_8.decode(buffer).toString();
            logger.info("from client: {} ", input);

            buffer.flip();
            inputBuffer.append(input);
        }

        if (reqestSize > 0) {
            var requestFromClient = inputBuffer.toString().replace("\n", "").replace("\r", "");
            logger.info("requestFromClient: {} ", requestFromClient);
            return requestFromClient;
        } else {
            logger.info("requestFromClient: is Empty");
            return null;
        }
    }

    private void sendResponse(SocketChannel socketChannel, String responseForClient) throws IOException {
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
    }

    private String processClientRequest(String input) {
        someWork();
        return String.format("echo: %s%n", input);
    }

    private void someWork() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
