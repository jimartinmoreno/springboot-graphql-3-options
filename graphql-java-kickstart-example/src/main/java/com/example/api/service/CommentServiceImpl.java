package com.example.api.service;

import com.example.api.client.CommentFeignClient;
import com.example.api.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentFeignClient commentClient;

    @Override
    public CompletableFuture<List<Comment>> getAllComments() {
        return commentClient.getAllComments();
    }

    @Override
    public CompletableFuture<Comment> getCommentById(Long id) {
        return commentClient.getCommentById(id);
    }

    @Override
    public CompletableFuture<List<Comment>> getAllCommentsByPostId(Long postId) {
        return commentClient.getCommentsByPostId(postId);
    }

    @Override
    public Comment createComment(Comment comment) {
        return commentClient.createComment(comment);
    }

    @Override
    public void updateComment(Long commentId, Comment comment) {
        commentClient.updateComment(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentClient.deleteComment(commentId);
    }

}
