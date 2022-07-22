package com.example.api.graphql.resovers.user;

import com.example.api.model.Company;
import com.example.api.model.User;
import graphql.kickstart.tools.GraphQLResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Sometimes, GraphQL object can have nested GraphQL object, which result in nested queries. We implement
 * GraphQLResolver<T> to resolve such nested Queries. For e.g. User can have multiple posts [Post]
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserNonParentCompanyResolver implements GraphQLResolver<Company> {

    private final Executor myExecutor;

    public CompletableFuture<String> getNonParentResolverTrait(Company company) {
        return CompletableFuture.supplyAsync(
                () -> {
                    log.info("Getting NonParentResolverTrait for company name: {} ", company.getName());
                    return "Non Parent Resolver Trait Value";
                }, myExecutor);
    }
}
