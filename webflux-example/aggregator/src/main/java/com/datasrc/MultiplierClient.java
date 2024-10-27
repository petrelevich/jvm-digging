package com.datasrc;

import org.springframework.web.reactive.function.client.WebClient;

public record MultiplierClient(WebClient client, String name) {
}
