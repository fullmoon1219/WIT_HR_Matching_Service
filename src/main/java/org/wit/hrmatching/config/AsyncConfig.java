package org.wit.hrmatching.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // 최소 스레드
        executor.setMaxPoolSize(5); // 최대 스레드
        executor.setQueueCapacity(100); // 최대 큐 용량
        executor.setThreadNamePrefix("Async-Mail-");
        executor.initialize();
        return executor;
    }
}
