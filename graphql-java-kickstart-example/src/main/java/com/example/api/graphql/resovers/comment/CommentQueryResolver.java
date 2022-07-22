package com.example.api.graphql.resovers.comment;

import com.example.api.model.Comment;
import com.example.api.service.CommentService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentQueryResolver implements GraphQLQueryResolver {

    private final CommentService commentService;

    CompletableFuture<List<Comment>> getComments() {
        return commentService.getAllComments();
    }

    CompletableFuture<Comment> getCommentById(Long commentId, DataFetchingEnvironment env) {
        log.info("Getting comment for commentId: {}", commentId);
        return commentService.getCommentById(commentId);
    }

    CompletableFuture<List<Comment>> getCommentsByPostId(Long postId) {
        return commentService.getAllCommentsByPostId(postId);
    }
}
