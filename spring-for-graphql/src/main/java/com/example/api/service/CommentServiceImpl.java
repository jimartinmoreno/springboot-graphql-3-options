package com.example.api.service;

import com.example.api.client.CommentFeignClient;
import com.example.api.model.Comment;
import com.example.api.model.Post;
import com.example.api.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public List<Comment> getAllCommentsByPostId(Long postId) {
        log.info("getAllCommentsByPostId - Requesting batch comments of posts id: {}", postId);
        return commentClient.getCommentsByPostId(postId);
    }

    public CompletableFuture<Map<Post, List<Comment>>> getAllCommentsByPostIds(Set<Post> posts) {
        log.info("getAllCommentsByPostIds - Requesting batch comments of post size: {}", posts.size());
        Map<Post, List<Comment>> result = new HashMap<>();
        posts.forEach(post -> result.put(post, commentClient.getCommentsByPostId(post.getId())));
        return CompletableFuture.supplyAsync(() -> result);
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
