package com.example.api.service;

import com.example.api.model.Post;
import com.example.api.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface PostService {

    List<Post> getAllPosts();

    CompletableFuture<Optional<Post>> getPostById(Long postId);

    List<Post> getAllPostsByUserId(Long userId);

    CompletableFuture<Map<User, List<Post>>> getAllPostsByUserIds(Set<User> users);

    Post createPost(Post post);

    void updatePost(Long postId, Post post);

    void deletePost(Long postId);
}
