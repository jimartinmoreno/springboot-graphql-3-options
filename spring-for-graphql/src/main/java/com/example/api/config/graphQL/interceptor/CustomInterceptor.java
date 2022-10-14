package com.example.api.config.graphQL.interceptor;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.api.config.graphQL.instrumentation.LoggingInstrumentation.CORRELATION_ID;

@Slf4j
@Component
public class CustomInterceptor implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {

        /**
         * Podemos incluir el correlationId aqui en el MDC y GraphQLContext o en el CorrelationIdFilter
         */
        String correlationId = Optional.ofNullable(MDC.get(CORRELATION_ID))
                .orElseGet(() -> {
                    log.info("intercept - orElseGet - CorrelationId: {}", request.getHeaders().getFirst(CORRELATION_ID));
                    MDC.put(CORRELATION_ID,
                            Optional.ofNullable(request.getHeaders().getFirst(CORRELATION_ID)).orElse(UUID.randomUUID().toString()));
                    return MDC.get(CORRELATION_ID);
                });

        request.configureExecutionInput((executionInput, builder) -> {
                    log.info("intercept - configureExecutionInput - correlationId: {}", correlationId);
                    return builder
                            .graphQLContext(Collections.singletonMap(CORRELATION_ID, correlationId))
                            .build();
                }
        );

        //        return chain.next(request).doOnNext(response -> {
        //            log.info("intercept - doOnNext - data: {}", response.getExecutionResult().getData().toString());
        //            log.info("intercept - doOnNext - context: {}", response.getExecutionInput().getGraphQLContext());
        //            String value = response.getExecutionInput().getGraphQLContext().get("cookieName");
        //            ResponseCookie cookie = ResponseCookie.from("cookieName", value).build();
        //            response.getResponseHeaders().add(HttpHeaders.SET_COOKIE, cookie.toString());
        //        });

        return chain.next(request).map(response -> {

            if (response.isValid()) {
                return response;
            }

            log.info("intercept - map - errors in: {}", response.getErrors());

            List<GraphQLError> errors = response.getErrors().stream()
                    .map(error -> {
                        log.info("intercept - map - error: {}", error.getMessage());
                        return GraphqlErrorBuilder.newError()
                                .message(error.getMessage())
                                .path(error.getParsedPath())
                                .errorType(error.getErrorType())
                                .extensions(error.getExtensions())
                                .locations(error.getLocations())
                                .build();
                    })
                    .collect(Collectors.toList());

            log.info("intercept - map - errors out: {}", errors);



            return response.transform(builder -> builder.errors(errors).build());
        });
    }
}
