package com.datasrc;

import static org.assertj.core.api.Assertions.assertThat;

import com.datasrc.producer.StringValue;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SourceDataControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void dataTest() {
        var seed = 10L;
        var dataLimit = 3;
        var timeOut = 20;
        var result = webTestClient
                .get()
                .uri(String.format("/data/%s", seed))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(StringValue.class)
                .getResponseBody()
                .take(dataLimit)
                .timeout(Duration.ofSeconds(timeOut))
                .collectList()
                .block();

        assertThat(result).hasSize(dataLimit);
    }
}
