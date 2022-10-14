package com.example.api.client;

import com.example.api.model.Comment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CommentFeignClient {

    @GetMapping("/comments")
    CompletableFuture<List<Comment>> getAllComments();

    @GetMapping("/comments/{commentId}")
    CompletableFuture<Comment> getCommentById(@PathVariable Long commentId);

    @GetMapping("/comments")
    List<Comment> getCommentsByPostId(@RequestParam Long postId);

    @PostMapping("/comments")
    Comment createComment(Comment comment);

    @PutMapping("/comments")
    Comment updateComment(Comment comment);

    @DeleteMapping("/comments/{commentId}")
    Comment deleteComment(@PathVariable Long commentId);
}
