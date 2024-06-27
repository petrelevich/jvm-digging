package com.datasrc.model;

public record Response(RequestId requestId, String producerName, ResponseId responseId, long data) {}
