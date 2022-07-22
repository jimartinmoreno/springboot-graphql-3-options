package com.example.api.service;

import com.example.api.model.Comment;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CommentService {

    CompletableFuture<List<Comment>> getAllComments();

    CompletableFuture<Comment> getCommentById(Long id);

    CompletableFuture<List<Comment>> getAllCommentsByPostId(Long commentId);

    Comment createComment(Comment comment);

    void updateComment(Long commentId, Comment comment);

    void deleteComment(Long commentId);

}
