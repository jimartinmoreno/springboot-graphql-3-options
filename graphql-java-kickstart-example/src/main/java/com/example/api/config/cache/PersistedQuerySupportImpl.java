package com.example.api.config.cache;

import graphql.ExecutionInput;
import graphql.execution.preparsed.persisted.InMemoryPersistedQueryCache;
import graphql.execution.preparsed.persisted.PersistedQuerySupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@Component
@Slf4j
public class PersistedQuerySupportImpl extends PersistedQuerySupport {

    public PersistedQuerySupportImpl(InMemoryPersistedQueryCache inMemoryPersistedQueryCache) {
        super(inMemoryPersistedQueryCache);
    }

    @Override
    protected Optional<Object> getPersistedQueryId(ExecutionInput executionInput) {
        return Optional.of(executionInput.getQuery());
    }
}
