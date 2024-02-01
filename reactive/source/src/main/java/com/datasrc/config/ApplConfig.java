package com.datasrc.config;

import com.datasrc.logger.MdcField;
import com.datasrc.logger.MdcLogger;
import com.datasrc.logger.MdcLoggerContext;
import io.netty.channel.nio.NioEventLoopGroup;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
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
    private static final int THREAD_POOL_SIZE = 2;

    public static final MdcField MDC_SEED = new MdcField("seed");

    @Bean("mdcFields")
    public List<MdcField> mdcFields() {
        return List.of(MDC_SEED);
    }

    @Bean
    public MdcLogger monoLoggerContext(List<MdcField> mdcFieldList) {
        return new MdcLoggerContext(mdcFieldList);
    }

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
    public Scheduler timer() {
        return Schedulers.newParallel("processor-thread", 2);
    }
}
