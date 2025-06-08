package ru.demo.echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/*
--add-exports java.base/jdk.internal.misc=ALL-UNNAMED -Dio.netty.tryReflectionSetAccessible=true
 */
@SuppressWarnings("java:S2093")
public class EchoServer {
    private static final Logger logger = LoggerFactory.getLogger(EchoServer.class);
    private static final int THREAD_POOL_SIZE = 2;
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        new EchoServer().start();
    }

    private void start() throws InterruptedException {
        var eventLoopGroup = new NioEventLoopGroup(THREAD_POOL_SIZE, new ThreadFactory() {
            private final AtomicLong threadIdGenerator = new AtomicLong(0);

            @Override
            public Thread newThread(@Nonnull Runnable task) {
                return new Thread(task, "event-loop-thread-" + threadIdGenerator.incrementAndGet());
            }
        });
        try {
            var serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(eventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(PORT))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });

            var channelFuture = serverBootstrap.bind();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    logger.info("waiting for client...");
                } else {
                    logger.error("error: {}", future.cause().getMessage());
                }
            });
            logger.info("binding...");

            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }
}
