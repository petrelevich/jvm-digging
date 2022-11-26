package ru.demo;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.eclipse.jetty.http.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(HelloServlet.class);
    private static final String QUERY_PARAMETER_NAME = "name";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("income request:{}", request);
        var name = request.getParameter(QUERY_PARAMETER_NAME);
        response.setContentType(MimeTypes.Type.TEXT_PLAIN.asString());
        response.getWriter().println(String.format("Hello, %s", name));
    }
}
