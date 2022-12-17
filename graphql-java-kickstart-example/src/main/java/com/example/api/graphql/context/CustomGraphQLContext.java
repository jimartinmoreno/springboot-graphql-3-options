package com.example.api.graphql.context;

import graphql.kickstart.execution.context.DefaultGraphQLContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.GraphQLServletContext;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;

import java.time.Instant;

/**
 * Custom Context
 */
@Builder
@Slf4j
@ToString
public class CustomGraphQLContext extends DefaultGraphQLContext implements GraphQLServletContext{

    @Delegate
    private final DefaultGraphQLServletContext defaultGraphQLServletContext;

    @Getter
    private final SecurityContext securityContext;

    @Getter
    private final String correlationId;

    @Getter
    private final Instant startTime;
}
