package com.example.api.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;

/**
 * Configuración para la ejecución asincrona
 */
@Configuration
public class AsyncExecutorConfig {

    @Bean
    public AsyncTaskExecutor myExecutor(ExecutorFactory executorFactory) {
        return executorFactory.asyncTaskExecutor();
    }
}
