package ru.demo.mainpackage.model;

public record Response(RequestId requestId, String producerName, ResponseId responseId, Long data) {}
