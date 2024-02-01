package com.datasrc.config;

import io.netty.channel.nio.NioEventLoopGroup;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

@Configuration
@SuppressWarnings("java:S2095")
public class ApplConfig {
    private static final int THREAD_POOL_SIZE = 2;

    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory() {
        var eventLoopGroup = new NioEventLoopGroup(THREAD_POOL_SIZE, new ThreadFactory() {
            private final AtomicLong threadIdGenerator = new AtomicLong(0);

            @Override
            public Thread newThread(@NonNull Runnable task) {
                return new Thread(task, "server-thread-" + threadIdGenerator.incrementAndGet());
            }
        });

        var factory = new NettyReactiveWebServerFactory();
        factory.addServerCustomizers(builder -> builder.runOn(eventLoopGroup));

        return factory;
    }

    @Bean
    public ReactorResourceFactory reactorResourceFactory() {
        var eventLoopGroup = new NioEventLoopGroup(THREAD_POOL_SIZE, new ThreadFactory() {
            private final AtomicLong threadIdGenerator = new AtomicLong(0);

            @Override
            public Thread newThread(@Nullable Runnable task) {
                var thread = new Thread(task);
                thread.setName("client-thread-" + threadIdGenerator.incrementAndGet());
                return thread;
            }
        });

        var resourceFactory = new ReactorResourceFactory();
        resourceFactory.setLoopResources(b -> eventLoopGroup);
        resourceFactory.setUseGlobalResources(false);
        return resourceFactory;
    }

    @Bean
    public ReactorClientHttpConnector reactorClientHttpConnector(ReactorResourceFactory resourceFactory) {
        return new ReactorClientHttpConnector(resourceFactory, mapper -> mapper);
    }

    @Bean
    public Scheduler timer() {
        return Schedulers.newParallel("processor-thread", 2);
    }
}
