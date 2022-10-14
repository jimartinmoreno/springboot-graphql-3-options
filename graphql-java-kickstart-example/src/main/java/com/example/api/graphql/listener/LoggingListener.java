package com.example.api.graphql.listener;

import graphql.kickstart.servlet.core.GraphQLServletListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;

import static com.example.api.graphql.instrumentation.RequestLoggingInstrumentation.CORRELATION_ID;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingListener implements GraphQLServletListener {


    @Override
    public RequestCallback onRequest(HttpServletRequest request, HttpServletResponse response) {
        log.info("onRequest - Received graphQL request");
        //String correlationId = Optional.ofNullable(request.getHeader(CORRELATION_ID))
        //       .orElse(UUID.randomUUID().toString());
        log.info("onRequest - CorrelationId from header: {}", request.getHeader(CORRELATION_ID));
        //log.info("build - CorrelationId final: {}", correlationId);

        /**
         * Relacionado con el Contexto e incluir el CorrelationId en los logs
         */
        var startTime = Instant.now();
        //        MDC.put(CORRELATION_ID, correlationId);
        //        MDC.put(START_TIME, startTime.toString());

        return new RequestCallback() {
            @Override
            public void onSuccess(HttpServletRequest request, HttpServletResponse response) {
                RequestCallback.super.onSuccess(request, response);
                log.info("Success on  Request, Time taken: {}", Duration.between(startTime, Instant.now()).toMillis());
            }

            @Override
            public void onError(HttpServletRequest request, HttpServletResponse response, Throwable throwable) {
                RequestCallback.super.onError(request, response, throwable);
                log.info("Error on Request, Time taken: {}", Duration.between(startTime, Instant.now()).toMillis());
            }

            @Override
            public void onFinally(HttpServletRequest request, HttpServletResponse response) {
                RequestCallback.super.onFinally(request, response);
                log.info("Completed Request, Time taken: {}", Duration.between(startTime, Instant.now()).toMillis());
            }
        };
    }


}
