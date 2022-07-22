package com.example.api.graphql.context;

import graphql.kickstart.execution.context.DefaultGraphQLContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.GraphQLServletContext;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoaderRegistry;
import org.springframework.security.core.context.SecurityContext;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
