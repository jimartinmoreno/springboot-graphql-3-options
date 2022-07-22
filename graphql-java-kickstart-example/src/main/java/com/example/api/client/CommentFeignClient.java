package com.example.api.client;

import com.example.api.model.Comment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

//@FeignClient(name = "commentFeignClient", url = "https://jsonplaceholder.typicode.com")
public interface CommentFeignClient {

    @GetMapping("/comments")
    CompletableFuture<List<Comment>> getAllComments();

    @GetMapping("/comments/{commentId}")
    CompletableFuture<Comment> getCommentById(@PathVariable Long commentId);

    @GetMapping("/comments")
    CompletableFuture<List<Comment>> getCommentsByPostId(@RequestParam Long postId);

    @PostMapping("/comments")
    Comment createComment(Comment comment);

    @PutMapping("/comments")
    Comment updateComment(Comment comment);

    @DeleteMapping("/comments/{commentId}")
    Comment deleteComment(@PathVariable Long commentId);
}
