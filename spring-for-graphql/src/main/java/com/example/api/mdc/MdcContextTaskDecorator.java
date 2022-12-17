package com.example.api.mdc;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * A decorator to be applied to any Runnable about to be executed.
 */
@Component
@Slf4j
public class MdcContextTaskDecorator implements TaskDecorator {

    /**
     * Propagate the current thread's MDC context to the target thread.
     */
    @NotNull
    @Override
    public Runnable decorate(@NotNull Runnable runnable) {
        // Lo usabamos al poner ahora el correlation ID en el CustomGraphQLContextBuilder
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        log.info("mdcContext: {}", mdcContext);
        return () -> {
            try {
                Optional.ofNullable(mdcContext).ifPresent(MDC::setContextMap);
                runnable.run();
            } catch (Exception e) {
                log.error("decorate exception: {}", e.getMessage());
            } finally {
                MDC.clear();
            }
        };
    }
}
