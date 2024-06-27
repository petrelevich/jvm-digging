package ru.demo.mainpackage.kafka;

public class ConsumerException extends RuntimeException {
    public ConsumerException(String message, Throwable cause) {
        super(message, cause);
    }
}
