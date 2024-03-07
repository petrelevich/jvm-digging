package ru.demo.services;

import java.sql.SQLException;

public class PaymentException extends RuntimeException {
    public PaymentException(String msg, SQLException ex) {
        super(msg, ex);
    }
}
