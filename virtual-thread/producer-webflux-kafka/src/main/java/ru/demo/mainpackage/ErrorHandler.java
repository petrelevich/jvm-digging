package ru.demo.mainpackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Component
public class ErrorHandler implements ErrorWebExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    @NonNull
    public Mono<Void> handle(@NonNull ServerWebExchange serverWebExchange, @NonNull Throwable thr) {
        log.error("Unhandled exception", thr);
        var bufferFactory = serverWebExchange.getResponse().bufferFactory();

        var response = serverWebExchange.getResponse();

        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        var dataBuffer = bufferFactory.wrap("Internal error".getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }
}
