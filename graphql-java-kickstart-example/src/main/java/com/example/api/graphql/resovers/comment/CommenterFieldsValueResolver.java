package com.example.api.graphql.resovers.comment;

import com.example.api.model.Comment;
import com.example.api.model.Commenter;
import graphql.GraphQLContext;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static com.example.api.graphql.instrumentation.RequestLoggingInstrumentation.SECURITY_CONTEXT;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommenterFieldsValueResolver implements GraphQLResolver<Commenter> {

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public Long getId(Commenter commenter) {
        log.info("Getting commenter Id: {},", commenter.getId());
        return commenter.getId();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public CompletableFuture<String> getName(Commenter commenter, DataFetchingEnvironment dataFetchingEnvironment) {
        log.info("getName - Getting commenter Name: {},", commenter.getName());
        GraphQLContext graphQlContext = dataFetchingEnvironment.getGraphQlContext();
        log.info("getName - securityContext: {},", graphQlContext);
        return completedFuture(commenter.getName());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public CompletableFuture<String> getEmail(Commenter commenter) {
        log.info("Getting commenter Email: {},", commenter.getEmail());
        return completedFuture(commenter.getEmail());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public CompletableFuture<Boolean> isPublish(Commenter commenter, DataFetchingEnvironment env) {
        //printAuthentication(env);
        log.info("Getting commenter publish: {},", commenter.isPublish());
        return completedFuture(commenter.isPublish());
    }

    private void printAuthentication(DataFetchingEnvironment env) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        log.info("authentication: {},", authentication);
        log.info("Source: {},", (Object) env.getSource());
        log.info("Root: {},", env.getRoot().getClass());
        log.info("FieldDefinition: {},", env.getFieldDefinition());
        log.info("Selections: {},", env.getOperationDefinition().getSelectionSet().getSelections());
    }
}
