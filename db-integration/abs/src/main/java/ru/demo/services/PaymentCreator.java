package ru.demo.services;

import ru.demo.model.Payment;

public interface PaymentCreator {
    long create(Payment payment);
}
