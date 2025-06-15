package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.server.ClientConnectionHandler;
import ru.demo.server.ClientRequestHandle;
import ru.demo.server.ClientRequestHandleFactory;
import ru.demo.server.EventLoop;
import ru.demo.server.NetworkException;
import ru.demo.server.queue.TaskQueueConsumerProducer;
import ru.demo.server.queue.TaskQueueJc;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;

public class SingleThreadEventLoop implements EventLoop {
    private static final Logger log = LoggerFactory.getLogger(SingleThreadEventLoop.class);
    private static final long START_TIME = System.nanoTime();

    private final Selector selector;
    private Thread thread;
    private final ClientConnectionHandler clientConnectionHandler;
    private final ClientRequestHandleFactory clientRequestHandleFactory;
    private final String name;
    private static final int IO_RATIO = 50;

    private final TaskQueueConsumerProducer taskQueueConsumerProducer;

    public SingleThreadEventLoop(String name,
                                 ClientRequestHandleFactory clientRequestHandleFactory,
                                 ClientConnectionHandler clientConnectionHandler) throws IOException {
        log.info("name:{}", name);
        this.clientConnectionHandler = clientConnectionHandler;
        this.clientRequestHandleFactory = clientRequestHandleFactory;
        this.name = name;
        this.selector = Selector.open();
        this.taskQueueConsumerProducer = new TaskQueueJc();
    }

    @Override
    public void start() {
        thread = Thread.ofPlatform().name(name).start(this::run);
    }

    @Override
    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public void registerClient(SocketChannel clientSocketChannel) {
        taskQueueConsumerProducer.offerTask(() -> {
            try {
                var selectionKey = clientSocketChannel.register(selector, OP_READ);
                var clientRequestHandle = clientRequestHandleFactory.newClientRequestHandle();
                selectionKey.attach(clientRequestHandle);
                log.info("registerClient done, key:{}", selectionKey);
            } catch (Exception ex) {
                log.error("client register error", ex);
            }
        });
    }

    @Override
    public void registerServer(ServerSocketChannel serverSocketChannel) {
        taskQueueConsumerProducer.offerTask(() -> {
            try {
                var key = serverSocketChannel.register(selector, OP_ACCEPT);
                log.info("registerServer done, key:{}", key.channel());
            } catch (Exception ex) {
                log.error("server register error", ex);
            }
        });
    }

    @Override
    public String getName() {
        return name;
    }

    private void performIO(SelectionKey selectedKey) {
        try {
            log.info("something happened, key:{}", selectedKey);
            if (selectedKey.isAcceptable()) {
                acceptConnection(selectedKey);
            } else if (selectedKey.isReadable()) {
                readWriteClient(selectedKey);
            }
        } catch (Exception ex) {
            throw new NetworkException(ex);
        }
    }

    private void acceptConnection(SelectionKey key) throws IOException {
        log.info("accept client connection, key:{}", key);
        var serverSocketChannel = (ServerSocketChannel) key.channel();
        var clientSocketChannel = serverSocketChannel.accept(); // The socket channel for the new connection
        clientSocketChannel.configureBlocking(false);
        log.info("socketChannel:{}", clientSocketChannel);

        clientConnectionHandler.accept(clientSocketChannel);
    }


    private void readWriteClient(SelectionKey selectionKey) throws IOException {
        log.info("read from client");
        var socketChannel = (SocketChannel) selectionKey.channel();

        var clientRequestHandleObj = selectionKey.attachment();
        if (clientRequestHandleObj == null) {
            throw new IllegalStateException("clientRequestHandle is null");
        }
        if (clientRequestHandleObj instanceof ClientRequestHandle clientRequestHandle) {
            try {
                clientRequestHandle.handle(taskQueueConsumerProducer, socketChannel);
            } catch (Exception ex) {
                if (ex instanceof IOException && "Broken pipe".equals(ex.getMessage())) {
                    log.error("Client disconnected");
                } else {
                    log.error("error sending response", ex);
                }
                socketChannel.close();
            }
        } else {
            throw new IllegalStateException("unknown clientRequestHandle type");
        }
    }

    private void run() {
        try {
            log.info("thread started, name:{}", name);
            while (!Thread.currentThread().isInterrupted()) {
                var ioStartTime = System.nanoTime();
                selectNow();
                var ioTime = System.nanoTime() - ioStartTime;
                runAllTasks(ioTime * (100 - IO_RATIO) / IO_RATIO);
            }
        } catch (Exception e) {
            log.error("thread run error", e);
        }

        try {
            selector.close();
        } catch (IOException e) {
            log.error("selector.close error", e);
        }
        log.info("thread stopped, name:{}", name);
    }

    private void runAllTasks(long timeoutNanos) {
        var task = taskQueueConsumerProducer.pollTask();
        if (task == null) {
            return;
        }

        long deadline = timeoutNanos > 0 ? getCurrentTimeNanos() + timeoutNanos : 0;
        long runTasks = 0;
        long lastExecutionTime;
        while (!Thread.currentThread().isInterrupted()) {
            safeExecute(task);

            runTasks++;

            // Check timeout every 64 tasks because nanoTime() is relatively expensive.
            // XXX: Hard-coded value - will make it configurable if it is really a problem.
            if ((runTasks & 0x3F) == 0) {
                lastExecutionTime = getCurrentTimeNanos();
                if (lastExecutionTime >= deadline) {
                    return;
                }
            }

            task = taskQueueConsumerProducer.pollTask();
            if (task == null) {
                return;
            }
        }
    }

    private long getCurrentTimeNanos() {
        return System.nanoTime() - START_TIME;
    }

    private static void safeExecute(Runnable task) {
        try {
            task.run();
        } catch (Exception t) {
            log.warn("A task raised an exception. Task: {}", task, t);
        }
    }

    private void selectNow() {
        try {
            selector.selectNow(this::performIO);
        } catch (Exception ex) {
            log.error("error:", ex);
        }
    }
}
