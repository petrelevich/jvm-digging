package ru.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.appl.EchoResponser;
import ru.demo.server.NioEventLoopGroup;

public class UpdatedServerNIO {
    private static final Logger log = LoggerFactory.getLogger(UpdatedServerNIO.class);

    private static final int PORT = 8080;
    private static final int N_THREADS = 2;
    private final ServerSocketChannel serverSocketChannel;
    private final NioEventLoopGroup eventLoopGroup;

    public static void main(String[] args) throws IOException {
        new UpdatedServerNIO();
    }

    public UpdatedServerNIO() throws IOException {
        log.info("starting, nThreads:{}, port:{}", N_THREADS, PORT);
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));

        eventLoopGroup = new NioEventLoopGroup(N_THREADS, serverSocketChannel, EchoResponser::new);

        var shutdownHook = new Thread(() -> {
            log.info("closing server");
            try {
                eventLoopGroup.stop();
            } catch (Exception ex) {
                log.error("eventLoopGroup.stop error", ex);
            }

            try {
                serverSocketChannel.close();
            } catch (Exception ex) {
                log.error("serverSocketChannel.close error", ex);
            }
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        log.info("server started");
    }

}
