package com.datasrc;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientDataControllerTest {

    private static WebClient client;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
        client = WebClient.create("http://localhost:" + wireMockServer.port());
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }


    @Test
    void dataTest() {
        //given
        var seed = 10L;
        var stringData1 = "str:1";
        var stringData2 = "str:2";
        var stringData3 = "str:3";
        var stringData4 = "str:4";

        stubFor(get(urlEqualTo(String.format("/data/%s", seed)))
                .willReturn(aResponse().withFixedDelay(1)
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_NDJSON_VALUE)
                        .withBody(
                                String.format("[ {\"value\":\"%s\"}, {\"value\":\"%s\"}, {\"value\":\"%s\"}, {\"value\":\"%s\"} ]",
                                        stringData1, stringData2, stringData3, stringData4)
                        )));

        var dataLimit = 4;
        var timeOut = 20;

        //when
        var result = client
                .get().uri(String.format("/data/%s", seed))
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(StringValue.class)
                .take(dataLimit)
                .timeout(Duration.ofSeconds(timeOut))
                .collectList()
                .block();

        //then
        assertThat(result).hasSize(dataLimit)
                .contains(new StringValue(stringData1),
                        new StringValue(stringData2),
                        new StringValue(stringData3),
                        new StringValue(stringData4));
    }
}