package com.example.api.graphql.controllers.post;

import com.example.api.model.Post;
import com.example.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostQueryController {

    private final PostService postService;

    @QueryMapping
    List<Post> getPosts() {
        return postService.getAllPosts();
    }

    @QueryMapping
    CompletableFuture<Optional<Post>> getPostById(Long postId) {
        return postService.getPostById(postId);
    }

    @QueryMapping
    List<Post> getPostsByUserId(Long userId) {
        return postService.getAllPostsByUserId(userId);
    }
}
