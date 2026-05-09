package ru.demo.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.reactor.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.reactive.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class ApplConfig {
    private static final int THREAD_POOL_SIZE = 2;

    @Bean(name = "executorIo", destroyMethod = "close")
    public ExecutorService executorIo() {
        return Executors.newFixedThreadPool(THREAD_POOL_SIZE, new ThreadFactory() {
            private final AtomicLong threadIdGenerator = new AtomicLong(0);

            @Override
            public Thread newThread(@NotNull Runnable task) {
                return new Thread(task, "server-thread-" + threadIdGenerator.incrementAndGet());
            }
        });
    }

    @Bean(destroyMethod = "close")
    public MultiThreadIoEventLoopGroup eventLoopGroup(@Qualifier("executorIo") ExecutorService executorIo) {
        return new MultiThreadIoEventLoopGroup(THREAD_POOL_SIZE, executorIo, NioIoHandler.newFactory());
    }

    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory(MultiThreadIoEventLoopGroup eventLoopGroup) {
        var factory = new NettyReactiveWebServerFactory();
        factory.addServerCustomizers(builder -> builder.runOn(eventLoopGroup));
        return factory;
    }

    @Bean
    public Scheduler workerPool() {
        return Schedulers.newParallel("worker-thread", THREAD_POOL_SIZE);
    }

    @Bean
    public ObjectMapper objectMapper() {
        JsonMapper mapper = JsonMapper.builder().build();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
