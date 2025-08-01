package ru.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private static final int CLIENTS_NUMBER = 20;
    private final String host;
    private final int port;


    public static void main(String[] args) throws InterruptedException {
        var counter = new AtomicInteger(0);
        String host = null;
        Integer port = null;
        if (args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }
        var allDoneLatch = new CountDownLatch(CLIENTS_NUMBER);
        long start = System.currentTimeMillis();
        for (var idx = 0; idx < CLIENTS_NUMBER; idx++) {
            var client = new Client(host, port);
            new Thread(() -> client.go("testData_" + counter.incrementAndGet(), allDoneLatch)).start();
        }
        var waitResult = allDoneLatch.await(60, TimeUnit.SECONDS);
        if (waitResult) {
            logger.info("all done");
        } else {
            logger.info("execution terminated");
        }
        logger.info("DONE in:{}", System.currentTimeMillis() - start);
    }

    public Client(String host, Integer port) {
        this.host = host == null ? "localhost" : host;
        this.port = port == null ? 8080: port;

        logger.info("host:{}, port:{}", host, port);
    }

    private void go(String request, CountDownLatch allDoneLatch) {
        try {
            try (var clientSocket = new Socket(host, port)) {
                var out = new PrintWriter(clientSocket.getOutputStream(), true);
                var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                logger.info("sending to server");
                out.println(request);
                var resp = in.readLine();
                logger.info("server response: {}", resp);
                out.println(String.format("%s-part2", request));
                var respPart2 = in.readLine();
                logger.info("server response_2: {}", respPart2);
                allDoneLatch.countDown();
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }
}
