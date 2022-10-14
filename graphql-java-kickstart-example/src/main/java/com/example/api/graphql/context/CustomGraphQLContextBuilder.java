package com.example.api.graphql.context;

import com.example.api.graphql.context.dataloader.DataLoaderRegistryFactory;
import graphql.kickstart.execution.context.DefaultGraphQLContextBuilder;
import graphql.kickstart.execution.context.GraphQLKickstartContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.example.api.graphql.instrumentation.RequestLoggingInstrumentation.CORRELATION_ID;
import static com.example.api.graphql.instrumentation.RequestLoggingInstrumentation.SECURITY_CONTEXT;

/**
 * Custom GraphQLServletContextBuilder
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomGraphQLContextBuilder extends DefaultGraphQLContextBuilder implements GraphQLServletContextBuilder {

    private final DataLoaderRegistryFactory dataLoaderRegistryFactory;

    /**
     * Return a CustomGraphQLContext
     */
    @Override
    public GraphQLKickstartContext build(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        String correlationId = Optional.ofNullable(httpServletRequest.getHeader(CORRELATION_ID))
                .orElse(UUID.randomUUID().toString());

        SecurityContext securityContext = SecurityContextHolder.getContext();

        /**
         * Relacionado con el Contexto e incluir el CorrelationId en los logs
         */
        MDC.put(CORRELATION_ID, correlationId);

        log.info("build - CorrelationId from header: {}", httpServletRequest.getHeader(CORRELATION_ID));
        log.info("build - CorrelationId final: {}", correlationId);
        log.info("build - MDC ContextMap: {}", MDC.getCopyOfContextMap());

        return GraphQLKickstartContext.of(dataLoaderRegistryFactory.create(),
                Map.of(httpServletRequest, httpServletRequest,
                        httpServletResponse, httpServletResponse,
                        SECURITY_CONTEXT, securityContext,
                        CORRELATION_ID, correlationId));

        //        DefaultGraphQLServletContext defaultGraphQLServletContext = DefaultGraphQLServletContext.createServletContext()
        //                .with(httpServletRequest)
        //                .with(httpServletResponse)
        //                .with(dataLoaderRegistryFactory.create())
        //                .build();

        //return defaultGraphQLServletContext;

        // return new CustomGraphQLContext(userId, context);
        //        return CustomGraphQLContext.builder()
        //                .defaultGraphQLServletContext(defaultGraphQLServletContext)
        //                .securityContext(SecurityContextHolder.getContext())
        //                .correlationId(correlationId)
        //                .startTime(Instant.now())
        //                .build();
    }

    @Override
    public GraphQLKickstartContext build(Session session, HandshakeRequest handshakeRequest) {
        var userId = handshakeRequest.getHeaders().get("user_id").get(0);
        log.info("build - CorrelationId from header: {}", handshakeRequest.getHeaders().get(CORRELATION_ID));

        String correlationId = Optional.ofNullable(handshakeRequest.getHeaders().get(CORRELATION_ID).toString())
                .orElse(UUID.randomUUID().toString());

        SecurityContext securityContext = SecurityContextHolder.getContext();

        MDC.put(CORRELATION_ID, correlationId);
        log.info("build - CorrelationId final: {}", handshakeRequest.getHeaders().get(CORRELATION_ID));
        log.info("MDC ContextMap: {}", MDC.getCopyOfContextMap());
        log.info("userId: {}", userId);

        return GraphQLKickstartContext.of(dataLoaderRegistryFactory.create(),
                Map.of(session, session,
                        handshakeRequest, handshakeRequest,
                        SECURITY_CONTEXT, securityContext,
                        CORRELATION_ID, correlationId
                ));

        //        return DefaultGraphQLWebSocketContext.createWebSocketContext()
        //                .with(session)
        //                .with(handshakeRequest)
        //                .with(dataLoaderRegistryFactory.create())
        //                .build();
    }

    @Override
    public GraphQLKickstartContext build() {
        return GraphQLKickstartContext.of(dataLoaderRegistryFactory.create());
        // return new DefaultGraphQLContext();
    }
}
