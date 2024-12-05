package ru.demo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class MdcFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(MdcFilter.class);
    private static final String HEADER_X_REQUEST_ID = "X-Request-Id";
    private static final String MDC_REQUEST_ID = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var xRequestId = request.getHeader(HEADER_X_REQUEST_ID);
        log.info("method:{}, xRequestId:{}", request.getMethod(), xRequestId);
        if (xRequestId != null) {
            MDC.put(MDC_REQUEST_ID, xRequestId);
        }

        var headerIterator = request.getHeaderNames().asIterator();
        while (headerIterator.hasNext()) {
            var header = headerIterator.next();
            log.info("request header:{}", header);
        }

        response.addHeader(HEADER_X_REQUEST_ID, xRequestId);
        filterChain.doFilter(request, response);
        MDC.remove(MDC_REQUEST_ID);
    }
}