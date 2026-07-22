package ru.demo.webserver;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerJetty {
    private static final Logger log = LoggerFactory.getLogger(ServerJetty.class);
    private final Server server;

    public ServerJetty(int serverPort, ApiHandler apiHandler) {
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setName("server");
        threadPool.setIdleTimeout(30_000);
        threadPool.setMaxThreads(20);
        threadPool.setMinThreads(20);

        server = new Server(threadPool);

        // The number of acceptor threads.
        // The number of acceptors may be set to 0. In this case, the ServerSocketChannel will be configured in
        // non-blocking mode, and registered in the SelectorManager.
        int acceptors = 0;

        // The number of selectors.
        int selectors = 1;

        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);
        HttpConnectionFactory http11 = new HttpConnectionFactory(httpConfig);

        ServerConnector connector = new ServerConnector(server, acceptors, selectors, http11);
        connector.setPort(serverPort);
        connector.setHost("localhost");
        connector.setAcceptQueueSize(128);

        server.addConnector(connector);
        ContextHandlerCollection contextCollection = new ContextHandlerCollection();

        // http://localhost:8080/api/v1/find/43
        var context = new ContextHandler(apiHandler, "/api/v1/find");
        context.setAllowNullPathInContext(true);
        contextCollection.setDynamic(false);
        contextCollection.addHandler(context);

        server.setHandler(contextCollection);
    }

    public void start() throws Exception {
        server.start();
        log.info("Server started");
    }

    public void stop() throws Exception {
        server.stop();
        log.info("Server stopped");
    }
}
