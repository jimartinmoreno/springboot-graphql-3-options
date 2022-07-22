package com.example.api.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuración para la ejecución asincrona de los Resolvers
 */
@Configuration
//@EnableAsync
public class AsyncExecutorConfig {

    @Bean
    public AsyncTaskExecutor myExecutor(ExecutorFactory executorFactory) {
        return executorFactory.newExecutor();
    }
}
