package com.example.api.config.async;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExecutorFactory {

    private final TaskDecorator mdcContextTaskDecorator;

    public AsyncTaskExecutor newExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
        executor.setKeepAliveSeconds(0);
        executor.setTaskDecorator(mdcContextTaskDecorator); // Specify a custom TaskDecorator to be applied to any Runnable about to be executed.
        executor.initialize();
        /**
         * An AsyncTaskExecutor which wraps each Runnable in a DelegatingSecurityContextRunnable and each Callable in a DelegatingSecurityContextCallable.
         * Needed to pass the security credentials of the authenticated user in asynchronous processes
         */
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
}
