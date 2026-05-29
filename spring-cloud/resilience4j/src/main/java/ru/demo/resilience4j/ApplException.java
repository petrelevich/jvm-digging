package ru.demo.resilience4j;

public class ApplException extends RuntimeException {
    public ApplException(String message) {
        super(message);
    }
}
