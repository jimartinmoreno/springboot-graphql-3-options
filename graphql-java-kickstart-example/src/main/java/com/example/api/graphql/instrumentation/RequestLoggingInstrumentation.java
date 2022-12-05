package com.example.api.graphql.instrumentation;

import graphql.ExecutionResult;
import graphql.GraphQLContext;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * An implementation of SimpleInstrumentation that logs the execution Id and execution duration.
 * SimpleInstrumentation: It can be used as a base for derived classes where you only implement the methods you want to
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RequestLoggingInstrumentation extends SimpleInstrumentation {

    public static final String CORRELATION_ID = "CorrelationId";
    public static final String SECURITY_CONTEXT = "SecurityContext";
    public static final String START_TIME = "startTime";

    /**
     * This is called right at the start of query execution and its the first step in the instrumentation chain.
     * @param parameters
     * @return
     */
    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        var start = Instant.now();
        log.info("beginExecution - MDC ContextMap: {}", MDC.getCopyOfContextMap());
        log.info("beginExecution - GraphQLContext: {}", parameters.getGraphQLContext());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        GraphQLContext graphQlContext = parameters.getGraphQLContext();
        //var executionId = parameters.getExecutionInput().getExecutionId();
        //var customGraphQLContext = (CustomGraphQLContext) parameters.getContext();
        //SecurityContextHolder.setContext(customGraphQLContext.getSecurityContext());

        graphQlContext.put(CORRELATION_ID, MDC.get(CORRELATION_ID));
        graphQlContext.put(START_TIME, start);
        graphQlContext.put(SECURITY_CONTEXT, securityContext);

        log.info("beginExecution - GraphQLContext: {}", parameters.getGraphQLContext());

        // MDC class hides and serves as a substitute for the underlying logging system's MDC implementation.
        // Put a diagnostic context value (the val parameter) as identified with the key parameter into the current
        // thread's diagnostic context map. The key parameter cannot be null. The val parameter can be null only if
        // the underlying implementation supports it.
        //MDC.put(CORRELATION_ID, executionId.toString());

        log.info("beginExecution - Operation: {} with variables: {}", parameters.getOperation(), parameters.getVariables());

        SimpleInstrumentationContext.noOp();

        return SimpleInstrumentationContext.whenCompleted((executionResult, throwable) -> {
            // This callback will occur in the resolver thread.
            var duration = Duration.between(start, Instant.now());
            if (throwable == null) {
                log.info("beginExecution - Completed successfully in: {}", duration.toMillis());
            } else {
                log.warn("beginExecution - Failed in: {}", duration.toMillis(), throwable);
            }
        });
    }

    @Override
    public InstrumentationContext<Object> beginFieldFetch(InstrumentationFieldFetchParameters parameters) {
        //var customGraphQLContext = (CustomGraphQLContext) parameters.getEnvironment().getContext();
        //log.info("beginFieldFetch - CustomGraphQLContext: {}", customGraphQLContext);
        //SecurityContextHolder.setContext(customGraphQLContext.getSecurityContext());
        // Necesario si queremos incluir el CorrelationId en los logs cuando usamos Completable Futures y cliente HTTP Async
        //MDC.put(CORRELATION_ID, customGraphQLContext.getCorrelationId());
        //log.info("beginFieldFetch - with context: {}", context);
        log.info("beginFieldFetch - GraphQlContext: {}", parameters.getEnvironment().getGraphQlContext());
        GraphQLContext graphQlContext = parameters.getEnvironment().getGraphQlContext();
        MDC.put(CORRELATION_ID, graphQlContext.get(CORRELATION_ID));
        log.info("beginFieldFetch - MDC ContextMap: {}", MDC.getCopyOfContextMap());
        return super.beginFieldFetch(parameters);
    }
}
