package ru.demo;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class NumbersServer {
    private static final Logger log = LoggerFactory.getLogger(NumbersServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        log.info("numbers Server is starting...");

        int port = ApplicationProperties.getServerPort();

        Server server = ServerBuilder.forPort(port)
                .addService(new NumbersServiceImpl())
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            log.info("Received shutdown request");
            server.shutdown();
            log.info("Server stopped");
        }));

        log.info("Server is waiting for client, port:{}", port);
        server.awaitTermination();
    }
}
