package com.datasrc;

import org.springframework.web.reactive.function.client.WebClient;

public record AdviserClient(WebClient client, String name) {
}
