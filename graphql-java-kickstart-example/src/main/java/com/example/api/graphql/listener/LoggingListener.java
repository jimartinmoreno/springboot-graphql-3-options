package com.example.api.graphql.listener;

import graphql.kickstart.servlet.core.GraphQLServletListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        log.info("onRequest - CorrelationId from header: {}", request.getHeader(CORRELATION_ID));
        log.info("onRequest - Origin-System-Id from header: {}", request.getHeader("Origin-System-Id"));

        /**
         * Relacionado con el Contexto e incluir el CorrelationId en los logs
         */
        var startTime = Instant.now();

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
