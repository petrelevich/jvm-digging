package ru.webflux.demo.config;

import io.netty.channel.nio.NioEventLoopGroup;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.NonBlocking;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.annotation.NonNull;

@Configuration
public class ApplConfig {
    private static final int THREAD_POOL_SIZE = 1;

    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory() {
        var eventLoopGroup = new NioEventLoopGroup(THREAD_POOL_SIZE,
                new ThreadFactory() {
                    private final AtomicLong threadIdGenerator = new AtomicLong(0);
                    @Override
                    public NonBlockingThread newThread(@NonNull Runnable task) {
                        return new NonBlockingThread(task, "server-thread-" + threadIdGenerator.incrementAndGet());
                    }
                });
        var factory = new NettyReactiveWebServerFactory();
        factory.addServerCustomizers(builder -> builder.runOn(eventLoopGroup));
        return factory;
    }

    @Bean(destroyMethod = "dispose")
    public Scheduler blockingPool() {
        return Schedulers.newBoundedElastic(
                10,
                100,
                "blocking-thread",
                20,
                false);
    }


    public static class NonBlockingThread extends Thread implements NonBlocking {
        public NonBlockingThread(Runnable target, String name) {
            super(target, name);
        }
    }
}
