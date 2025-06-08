package ru.demo.appl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.server.ClientRequestHandle;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class EchoResponser implements ClientRequestHandle {
    private static final Logger log = LoggerFactory.getLogger(EchoResponser.class);


    public EchoResponser() {
        log.info("new EchoResponser");
    }

    @Override
    public void handle(SocketChannel clientSocketChannel) throws IOException {
        var requestFromClient = handleRequestInternal(clientSocketChannel);
        if (requestFromClient != null) {
            var responseForClient = processClientRequest(requestFromClient);
            sendResponse(clientSocketChannel, responseForClient);
        } else {
            clientSocketChannel.close();
        }
    }

    private String handleRequestInternal(SocketChannel socketChannel) throws IOException {
        var buffer = ByteBuffer.allocate(20);
        var inputBuffer = new StringBuilder(100);

        var reqestSize = 0;
        var readBytes = 0;
        while ((readBytes = socketChannel.read(buffer)) > 0) {
            reqestSize += readBytes;
            buffer.flip();
            var input = StandardCharsets.UTF_8.decode(buffer).toString();
            log.info("from client: {} ", input);

            buffer.flip();
            inputBuffer.append(input);
        }

        if (reqestSize > 0) {
            var requestFromClient = inputBuffer.toString().replace("\n", "").replace("\r", "");
            log.info("requestFromClient: {} ", requestFromClient);
            return requestFromClient;
        } else {
            log.info("requestFromClient: is Empty");
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
