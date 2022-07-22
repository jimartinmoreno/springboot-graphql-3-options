package com.example.api.graphql.resovers.comment;

import com.example.api.model.AgreementCountry;
import com.example.api.model.Comment;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
@RequiredArgsConstructor
@Slf4j
public class TermLoanFrameworkAgreementResolver implements GraphQLResolver<Comment> {
    private final Executor myExecutor;

    public CompletableFuture<String> getTermLoanFrameworkAgreementAcceptAt(Comment comment, AgreementCountry country, DataFetchingEnvironment env) {
        printAuthentication(env);
        return CompletableFuture.supplyAsync(
                () -> {
                    log.info("Getting termLoanFrameworkAgreementAcceptAt for comment id: {}, and country: {}", comment.getId(), country);
                    return Instant.EPOCH.toString();
                }, myExecutor);
    }

    private void printAuthentication(DataFetchingEnvironment env) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        log.info("authentication: {},", authentication);
        log.info("Source: {},", (Object) env.getSource());
        log.info("Root: {},", env.getRoot().getClass());
        log.info("FieldDefinition: {},", env.getFieldDefinition());
        log.info("Selections: {},", env.getOperationDefinition().getSelectionSet().getSelections());
        log.info("Arguments: {},", env.getArguments().keySet());
        log.info("country: {},", env.getArgument("country").toString());
    }
}
