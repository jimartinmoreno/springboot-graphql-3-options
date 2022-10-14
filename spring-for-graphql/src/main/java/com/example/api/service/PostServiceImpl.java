package com.example.api.service;

import com.example.api.client.PostFeignClient;
import com.example.api.model.Post;
import com.example.api.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostFeignClient postFeignClient;

    @Override
    public List<Post> getAllPosts() {
        return postFeignClient.getAllPosts();
    }

    @Override
    public CompletableFuture<Optional<Post>> getPostById(Long postId) {
        return postFeignClient.getPostById(postId);
    }

    @Override
    public List<Post> getAllPostsByUserId(Long userId) {
        log.info("getAllPostsByUserId - Requesting posts of userId: {}", userId);
        return postFeignClient.getPostByUserId(userId);
    }

    @Override
    public Post createPost(Post post) {
        return postFeignClient.createPost(post);
    }

    @Override
    public void updatePost(Long postId, Post post) {
        postFeignClient.updatePost(post);
    }

    @Override
    public void deletePost(Long postId) {
        postFeignClient.deletePost(postId);
    }

    @Override
    public CompletableFuture<Map<User, List<Post>>> getAllPostsByUserIds(Set<User> users) {
        log.info("getAllPostsByUserIds - Requesting batch post of user size: {}", users.size());
        Map<User, List<Post>> result = new HashMap<>();
        users.forEach(user -> result.put(user, postFeignClient.getPostByUserId(user.getId())));
        return CompletableFuture.supplyAsync(() -> result);
    }
}
