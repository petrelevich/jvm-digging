package ru.demo;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class JettyDemo {
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        var server = new Server(PORT);
        var context = new ServletContextHandler(server, "/hello");
        context.addServlet(HelloServlet.class, "/*");
        server.start();
    }
}
