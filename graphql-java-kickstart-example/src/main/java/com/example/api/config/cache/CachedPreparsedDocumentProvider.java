package com.example.api.config.cache;

import com.github.benmanes.caffeine.cache.Cache;
import graphql.ExecutionInput;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import graphql.execution.preparsed.PreparsedDocumentProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class CachedPreparsedDocumentProvider implements PreparsedDocumentProvider {

    @Autowired
    private final Cache<String, PreparsedDocumentEntry> queryCache;

    @Override
    public PreparsedDocumentEntry getDocument(ExecutionInput executionInput, Function<ExecutionInput, PreparsedDocumentEntry> parseAndValidateFunction) {

        Function<String, PreparsedDocumentEntry> mapCompute = key -> {
            PreparsedDocumentEntry preparsedDocumentEntry = parseAndValidateFunction.apply(executionInput);
            log.info("###### CACHING document: {}", preparsedDocumentEntry.getDocument());
            log.info("###### CACHING preparsedDocumentEntry Id: {}", System.identityHashCode(preparsedDocumentEntry));
            return preparsedDocumentEntry;
        };

        if (executionInput.getVariables().size() != 0) {
            PreparsedDocumentEntry preparsedDocumentEntry = queryCache.get(executionInput.getQuery(), mapCompute);
            log.info("###### CACHE estimated size: {}", queryCache.estimatedSize());
            log.info("###### CACHED document: {}", preparsedDocumentEntry.getDocument());
            log.info("###### CACHED preparsedDocumentEntry Id: {}", System.identityHashCode(preparsedDocumentEntry));
            return preparsedDocumentEntry;
        } else {
            PreparsedDocumentEntry preparsedDocumentEntry = parseAndValidateFunction.apply(executionInput);
            log.info("###### NO CACHED estimated size: {}", queryCache.estimatedSize());
            log.info("###### NO CACHED document: {}", preparsedDocumentEntry.getDocument());
            return preparsedDocumentEntry;
        }
    }
}
