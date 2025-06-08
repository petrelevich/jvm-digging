package ru.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    private static final int CLIENTS_NUMBER = 1;

    public static void main(String[] args) {
        var counter = new AtomicInteger(0);
        for (var idx = 0; idx < CLIENTS_NUMBER; idx++) {
            new Thread(() -> new Client().go("testData_" + counter.incrementAndGet())).start();
        }
    }

    private void go(String request) {
        try {
            try (var clientSocket = new Socket(HOST, PORT)) {
                var out = new PrintWriter(clientSocket.getOutputStream(), true);
                var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                logger.info("sending to server");
                out.println(request);
                var resp = in.readLine();
                logger.info("server response: {}", resp);
                out.println(String.format("%s-part2", request));
                var respPart2 = in.readLine();
                logger.info("server response_2: {}", respPart2);
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }
}
