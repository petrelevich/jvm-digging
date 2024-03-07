package ru.demo.model;

import java.math.BigDecimal;

public record Payment(String cardAccount, BigDecimal amount) {}
