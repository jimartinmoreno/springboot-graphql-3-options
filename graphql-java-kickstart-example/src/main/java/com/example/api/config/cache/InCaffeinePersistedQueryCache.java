package com.example.api.config.cache;

import com.github.benmanes.caffeine.cache.Cache;
import graphql.ExecutionInput;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import graphql.execution.preparsed.persisted.PersistedQueryCache;
import graphql.execution.preparsed.persisted.PersistedQueryCacheMiss;
import graphql.execution.preparsed.persisted.PersistedQueryNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

//@Component
@RequiredArgsConstructor
@Slf4j
public class InCaffeinePersistedQueryCache  implements PersistedQueryCache {

    private final Cache<String, PreparsedDocumentEntry> queryCache;

    @Override
    public PreparsedDocumentEntry getPersistedQueryDocument(Object persistedQueryId, ExecutionInput executionInput, PersistedQueryCacheMiss onCacheMiss) throws PersistedQueryNotFound {

        String queryText = executionInput.getQuery();

        Function<String, PreparsedDocumentEntry> mapCompute = key -> {
            log.warn("###### CACHING QUERY: {}", key);
            return onCacheMiss.apply(queryText);
        };

        if (executionInput.getVariables().size() != 0) {
            log.info("###### CACHE estimatedSize: {}", queryCache.estimatedSize());
            log.info("###### CACHE content: {}", queryCache.asMap().keySet());
            return queryCache.get(executionInput.getQuery(), mapCompute);
        } else {
            log.info("###### NO CACHE estimatedSize: {}", queryCache.estimatedSize());
            return onCacheMiss.apply(queryText);
        }
    }
}
