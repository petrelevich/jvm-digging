package ru.demo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.SingleThreadEventLoop;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

public class NioEventLoopGroup {
    private static final Logger log = LoggerFactory.getLogger(NioEventLoopGroup.class);
    private final EventLoop[] children;
    private final EventLoopChooser chooser;

    public NioEventLoopGroup(int nThreads,
                             ServerSocketChannel serverSocketChannel,
                             ClientRequestHandleFactory clientRequestHandleFactory) throws IOException {
        children = new EventLoop[nThreads];
        for (int i = 0; i < nThreads; i ++) {
            var eventLoop = newChild(clientRequestHandleFactory, String.format("event-loop-thread-%d", i));
            eventLoop.start();
            children[i] = eventLoop;
        }
        chooser = DefaultEventLoopChooserFactory.INSTANCE.newChooser(children);
        next().registerServer(serverSocketChannel);
    }

    private EventLoop newChild(ClientRequestHandleFactory clientRequestHandleFactory, String name) throws IOException {
        return new SingleThreadEventLoop(name, clientRequestHandleFactory,
                clientSocketChannel -> next().registerClient(clientSocketChannel));
    }


    private EventLoop next() {
        return chooser.next();
    }

    public void stop() {
        for(var eventLoop: children) {
            try {
                eventLoop.stop();
            } catch (Exception ex) {
                log.error("eventLoop:{} stop error", eventLoop.getName(), ex);
            }
        }
    }
}
