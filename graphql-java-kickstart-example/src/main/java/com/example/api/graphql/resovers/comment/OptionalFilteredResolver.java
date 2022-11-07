package com.example.api.graphql.resovers.comment;

import com.example.api.model.Comment;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
@RequiredArgsConstructor
@Slf4j
public class OptionalFilteredResolver implements GraphQLResolver<Comment> {
    private final Executor myExecutor;

    public CompletableFuture<String> getOptionalFilterField(Comment comment, String filter) {
        return CompletableFuture.supplyAsync(
                () -> {
                    log.info("Getting getOptionalFilterField for comment id: {}, and filter: {}", comment.getId(), filter);
                    return Instant.EPOCH.toString();
                }, myExecutor);
    }
}
