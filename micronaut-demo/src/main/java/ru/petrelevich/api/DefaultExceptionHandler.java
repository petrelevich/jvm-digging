package ru.petrelevich.api;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DefaultExceptionHandler implements ExceptionHandler<Exception, HttpResponse<String>> {
    private static final Logger log = LoggerFactory.getLogger(DefaultExceptionHandler.class);


    @Override
    public HttpResponse<String> handle(HttpRequest request, Exception exception) {
        log.error("request:{}", request, exception);
        return HttpResponse.serverError("internal error");
    }
}
