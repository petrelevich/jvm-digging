package com.datasrc.config;

import com.datasrc.MultiplierClient;
import io.netty.channel.nio.NioEventLoopGroup;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ReactorResourceFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.annotation.NonNull;

@Configuration
public class ApplConfig {
    private static final int THREAD_POOL_SIZE = 1;

    @Bean(name = "serverThreadEventLoop", destroyMethod = "close")
    public NioEventLoopGroup serverThreadEventLoop() {
        return new NioEventLoopGroup(THREAD_POOL_SIZE, new ThreadFactory() {
            private final AtomicLong threadIdGenerator = new AtomicLong(0);

            @Override
            public Thread newThread(@NonNull Runnable task) {
                return Thread.ofPlatform()
                        .name("server-handler-" + threadIdGenerator.incrementAndGet())
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

    @Bean(name = "clientThreadEventLoop", destroyMethod = "close")
    public NioEventLoopGroup clientThreadEventLoop() {
        return new NioEventLoopGroup(THREAD_POOL_SIZE, new ThreadFactory() {
            private final AtomicLong threadIdGenerator = new AtomicLong(0);

            @Override
            public Thread newThread(@NonNull Runnable task) {
                return new Thread(task, "client-handler-" + threadIdGenerator.incrementAndGet());
            }
        });
    }

    @Bean
    public ReactorResourceFactory reactorResourceFactory(
            @Qualifier("clientThreadEventLoop") NioEventLoopGroup clientThreadEventLoop) {
        var resourceFactory = new ReactorResourceFactory();
        resourceFactory.setLoopResources(b -> clientThreadEventLoop);
        resourceFactory.setUseGlobalResources(false);
        return resourceFactory;
    }

    @Bean
    public ReactorClientHttpConnector reactorClientHttpConnector(ReactorResourceFactory resourceFactory) {
        return new ReactorClientHttpConnector(resourceFactory, mapper -> mapper);
    }

    @Bean("multiplierClient1")
    public MultiplierClient multiplierClient1(
            WebClient.Builder builder, @Value("${application.multiplier-1}") String url) {
        return new MultiplierClient(builder.baseUrl(url).build(), "multiplierClient1");
    }

    @Bean("multiplierClient2")
    public MultiplierClient multiplierClient2(
            WebClient.Builder builder, @Value("${application.multiplier-2}") String url) {
        return new MultiplierClient(builder.baseUrl(url).build(), "multiplierClient2");
    }

    @Bean("multiplierClient3")
    public MultiplierClient multiplierClient3(
            WebClient.Builder builder, @Value("${application.multiplier-3}") String url) {
        return new MultiplierClient(builder.baseUrl(url).build(), "multiplierClient3");
    }
}