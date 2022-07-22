package com.example.api.config.cache;

import com.example.api.cache.RequestKey;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import graphql.execution.preparsed.persisted.InMemoryPersistedQueryCache;
import graphql.kickstart.autoconfigure.web.servlet.metrics.MetricsInstrumentation;
import graphql.kickstart.execution.config.ExecutionStrategyProvider;
import graphql.kickstart.execution.config.GraphQLBuilder;
import graphql.kickstart.execution.config.GraphQLBuilderConfigurer;
import graphql.kickstart.servlet.cache.CachedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Slf4j
@Configuration
public class CacheConfig {

    //@Bean
    public GraphQLBuilder graphQLBuilder(
            ExecutionStrategyProvider executionStrategyProvider,
            @Autowired(required = false) List<Instrumentation> instrumentations,
            @Autowired CachedPreparsedDocumentProvider cachedPreparsedDocumentProvider,
            @Autowired(required = false) GraphQLBuilderConfigurer graphQLBuilderConfigurer) {

        log.info("###### graphQLBuilder preparsedDocumentProvider: {}", cachedPreparsedDocumentProvider);

        GraphQLBuilder graphQLBuilder = new GraphQLBuilder();

        graphQLBuilder.executionStrategyProvider(() -> executionStrategyProvider);

        if (instrumentations != null && !instrumentations.isEmpty()) {
            if (instrumentations.size() == 1) {
                graphQLBuilder.instrumentation(() -> instrumentations.get(0));
            } else {
                instrumentations.sort((a, b) -> a instanceof MetricsInstrumentation ? 1 : 0);
                graphQLBuilder.instrumentation(() -> new ChainedInstrumentation(instrumentations));
            }
        }

        graphQLBuilder.preparsedDocumentProvider(() -> cachedPreparsedDocumentProvider);

        if (graphQLBuilderConfigurer != null) {
            graphQLBuilder.graphQLBuilderConfigurer(() -> graphQLBuilderConfigurer);
        }

        log.info("###### graphQLBuilder {} ######", graphQLBuilder);
        return graphQLBuilder;
    }

    /**
     * A builder of Cache, LoadingCache, AsyncCache, and AsyncLoadingCache instances having a combination of the following
     * features:
     * - automatic loading of entries into the cache, optionally asynchronously
     * - size-based eviction when a maximum is exceeded based on frequency and recency
     * - time-based expiration of entries, measured since last access or last write
     * - asynchronously refresh when the first stale request for an entry occurs
     * - keys automatically wrapped in weak references
     * - values automatically wrapped in weak or soft references
     * - writes propagated to an external resource
     * - notification of evicted (or otherwise removed) entries
     * - accumulation of cache access statistics
     *
     * @return Cache<RequestKey, CachedResponse>
     */
    @Bean
    public Cache<RequestKey, CachedResponse> responseCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(1L))   // Specifies that each entry should be automatically removed
                // from the cache once a fixed duration has elapsed
                .maximumSize(100L) // Specifies the maximum number of entries the cache may contain.
                .removalListener((key, value, cause) ->
                        log.info("###### Key {} with value {} was removed from the response cache. Cause {} ######", key, value, cause)
                )   // Specifies a listener instance that caches should notify each time an entry is removed for any reason.
                .build();
    }

    @Bean
    public Cache<String, PreparsedDocumentEntry> queryCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(1L))   // Specifies that each entry should be automatically removed
                .maximumSize(10) // Specifies the maximum number of entries the cache may contain.
                .removalListener((key, value, cause) ->
                        log.warn("###### Removed Key {} with value {} from the query Cache. Cause {} ######", key, value, cause)
                )   // Specifies a listener instance that caches should notify each time an entry is removed for any reason.
                .evictionListener((key, value, cause) ->
                        log.debug("###### Evicted Key {} with value {} from the query Cache. Cause {} ######", key, value, cause)
                )
                .build();
    }

    //@Bean
    public InMemoryPersistedQueryCache inMemoryPersistedQueryCache(){
        return InMemoryPersistedQueryCache.newInMemoryPersistedQueryCache().build();
    }
}
