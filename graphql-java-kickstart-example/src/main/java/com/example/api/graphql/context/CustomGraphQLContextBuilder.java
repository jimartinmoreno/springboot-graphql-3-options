package com.example.api.graphql.context;

import com.example.api.graphql.context.dataloader.DataLoaderRegistryFactory;
import graphql.kickstart.execution.context.DefaultGraphQLContext;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.DefaultGraphQLWebSocketContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.example.api.config.security.GraphQLSecurityConfig.USER_ID_PRE_AUTH_HEADER;
import static com.example.api.graphql.instrumentation.RequestLoggingInstrumentation.CORRELATION_ID;
import static com.example.api.graphql.instrumentation.RequestLoggingInstrumentation.SECURITY_CONTEXT;
import static java.util.Optional.ofNullable;

/**
 * Custom GraphQLServletContextBuilder
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomGraphQLContextBuilder implements GraphQLServletContextBuilder {

    private final DataLoaderRegistryFactory dataLoaderRegistryFactory;

    /**
     * Return a CustomGraphQLContext
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @Override
    public GraphQLContext build(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        String correlationId = Optional.ofNullable(httpServletRequest.getHeader(CORRELATION_ID))
                .orElse(UUID.randomUUID().toString());
        /**
         * Relacionado con el Contexto e incluir el CorrelationId en los logs
         */
        MDC.put(CORRELATION_ID, correlationId);
        httpServletRequest.setAttribute(CORRELATION_ID, correlationId);

        log.info("build - CorrelationId from header: {}", httpServletRequest.getHeader(CORRELATION_ID));
        log.info("build - CorrelationId final: {}", correlationId);
        log.info("build - MDC ContextMap: {}", MDC.getCopyOfContextMap());
        log.info("build - AttributeNames: {}", httpServletRequest.getAttributeNames());

        DefaultGraphQLServletContext defaultGraphQLServletContext = DefaultGraphQLServletContext.createServletContext()
                .with(httpServletRequest)
                .with(httpServletResponse)
                .with(dataLoaderRegistryFactory.create())
                .build();

        return defaultGraphQLServletContext;

        // return new CustomGraphQLContext(userId, context);
        //        return CustomGraphQLContext.builder()
        //                .defaultGraphQLServletContext(defaultGraphQLServletContext)
        //                .securityContext(SecurityContextHolder.getContext())
        //                .correlationId(correlationId)
        //                .startTime(Instant.now())
        //                .build();
    }

    @Override
    public GraphQLContext build(Session session, HandshakeRequest handshakeRequest) {
        var userId = handshakeRequest.getHeaders().get("user_id").get(0);
        log.info("build - CorrelationId from header: {}", handshakeRequest.getHeaders().get(CORRELATION_ID));

        String correlationId = Optional.ofNullable(handshakeRequest.getHeaders().get(CORRELATION_ID).toString())
                .orElse(UUID.randomUUID().toString());

        MDC.put(CORRELATION_ID, correlationId);
        log.info("build - CorrelationId final: {}", handshakeRequest.getHeaders().get(CORRELATION_ID));
        log.info("MDC ContextMap: {}", MDC.getCopyOfContextMap());
        log.info("userId: {}", userId);

        return DefaultGraphQLWebSocketContext.createWebSocketContext()
                .with(session)
                .with(handshakeRequest)
                .with(dataLoaderRegistryFactory.create())
                .build();
    }

    @Override
    public GraphQLContext build() {
        return new DefaultGraphQLContext();
    }
}
