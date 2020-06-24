package ru.usermng.dao;

public class UserOperationException extends RuntimeException {
    public UserOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
