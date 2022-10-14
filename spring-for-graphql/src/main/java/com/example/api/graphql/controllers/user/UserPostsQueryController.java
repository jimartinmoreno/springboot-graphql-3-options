package com.example.api.graphql.controllers.user;

import com.example.api.model.Post;
import com.example.api.model.User;
import com.example.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@Slf4j
public class UserPostsQueryController {

    private final PostService postService;

    /**
     * Dataloader que solo se ejecuta una sola vez para todos los users
     */
    //@BatchMapping(typeName = "User", field= "posts")
    @BatchMapping
    public Mono<Map<User, List<Post>>> posts(List<User> users) {
        log.info("getPostsDataloader - Getting post for user size: {}", users.size());
        Set<User> userSet = new HashSet<>(users);

        Supplier<CompletableFuture<Map<User, List<Post>>>> supplier = () -> postService.getAllPostsByUserIds(userSet);
        return Mono.fromFuture(supplier);
    }

    /**
     * Se ejecuta una vez por cada usuer
     */
//    @SchemaMapping(typeName = "User", field= "posts")
//    public List<Post> getUserPost(User user) {
//        log.info("getUserPost for user: {}", user);
//        return postService.getAllPostsByUserId(user.getId());
//    }
}
