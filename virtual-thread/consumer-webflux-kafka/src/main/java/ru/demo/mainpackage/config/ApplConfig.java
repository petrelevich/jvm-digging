package ru.demo.mainpackage.config;

import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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
import ru.demo.mainpackage.kafka.KafkaConsumer;
import ru.demo.mainpackage.ResponseStorage;
import ru.demo.mainpackage.kafka.KafkaProducer;

@Configuration
public class ApplConfig {
    private static final int THREAD_POOL_SIZE = 4;

    @Bean(name = "serverThreadEventLoop", destroyMethod = "close")
    public NioEventLoopGroup serverThreadEventLoop() {
        return new NioEventLoopGroup(THREAD_POOL_SIZE, new ThreadFactory() {
            private final AtomicLong threadIdGenerator = new AtomicLong(0);

            @Override
            public Thread newThread(@NonNull Runnable task) {
                return new Thread(task, "server-thread-" + threadIdGenerator.incrementAndGet());
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

    @Bean
    public ResponseStorage stringValueStorage(Scheduler timeoutTimer) {
        return new ResponseStorage(timeoutTimer);
    }

    @Bean(destroyMethod = "close")
    public KafkaConsumer kafkaConsumer(@Value("${application.kafka-bootstrap-servers}") String bootstrapServers,
                                       @Value("${application.topic-response}") String topicResponse,
                                       ResponseStorage stringValueStorage
                             ) {
        var consumer = new KafkaConsumer(bootstrapServers, topicResponse);
        Thread.ofVirtual().name("kafka-consumer").start(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                var responses = consumer.poll();
                stringValueStorage.put(responses);
            }
        });

        return consumer;
    }

    @Bean(destroyMethod = "close")
    public KafkaProducer kafkaProducer(@Value("${application.kafka-bootstrap-servers}") String bootstrapServers,
                                       @Value("${application.topic-request}") String topicRequest) {
        return new KafkaProducer(bootstrapServers, topicRequest);
    }

    @Bean
    public Scheduler timeoutTimer() {
        return Schedulers.newBoundedElastic(50, 1000, "timeout-Scheduler", 5, true);
    }
}
