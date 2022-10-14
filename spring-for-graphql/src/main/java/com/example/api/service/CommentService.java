package com.example.api.service;

import com.example.api.model.Comment;
import com.example.api.model.Post;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface CommentService {

    CompletableFuture<List<Comment>> getAllComments();

    CompletableFuture<Comment> getCommentById(Long id);

    List<Comment> getAllCommentsByPostId(Long commentId);

    CompletableFuture<Map<Post, List<Comment>>> getAllCommentsByPostIds(Set<Post> posts);

    Comment createComment(Comment comment);

    void updateComment(Long commentId, Comment comment);

    void deleteComment(Long commentId);

}
