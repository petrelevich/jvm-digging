package ru.demo.mainpackage.config;

import io.netty.channel.nio.NioEventLoopGroup;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.annotation.NonNull;

@Configuration
@SuppressWarnings("java:S2095")
public class ApplConfig {
    private static final int THREAD_POOL_SIZE = 1;
    private static final int KAFKA_POOL_SIZE = 1;

    @Bean(name = "serverThreadEventLoop", destroyMethod = "close")
    public NioEventLoopGroup serverThreadEventLoop() {
        return new NioEventLoopGroup(THREAD_POOL_SIZE, new ThreadFactory() {
            private final AtomicLong threadIdGenerator = new AtomicLong(0);

            @Override
            public Thread newThread(@NonNull Runnable task) {
                return Thread.ofPlatform()
                        .name("kafka-scheduler-" + threadIdGenerator.incrementAndGet())
                        .unstarted(task);
            }
        });
    }

    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory(
            @Qualifier("serverThreadEventLoop") NioEventLoopGroup serverThreadEventLoop) {
        var factory = new NettyReactiveWebServerFactory();
        factory.addServerCustomizers(builder -> builder.runOn(serverThreadEventLoop));
        return factory;
    }

    @Bean("kafkaScheduler")
    public Scheduler kafkaScheduler() {
        return Schedulers.newParallel(
                KAFKA_POOL_SIZE,
                task -> Thread.ofPlatform().name("kafka-scheduler-").unstarted(task));
    }

    @Bean(destroyMethod = "close")
    public ReactiveSender requestSender(
            @Value("${application.kafka-bootstrap-servers}") String bootstrapServers,
            @Value("${application.topic-request}") String topicRequest,
            @Qualifier("kafkaScheduler") Scheduler kafkaScheduler) {
        return new ReactiveSender(bootstrapServers, kafkaScheduler, topicRequest);
    }
}
