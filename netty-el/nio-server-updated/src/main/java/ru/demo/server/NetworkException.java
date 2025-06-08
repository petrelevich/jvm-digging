package ru.demo.server;

public class NetworkException extends RuntimeException {
    public NetworkException(Throwable cause) {
        super(cause);
    }

    public NetworkException(String message) {
        super(message);
    }
}
