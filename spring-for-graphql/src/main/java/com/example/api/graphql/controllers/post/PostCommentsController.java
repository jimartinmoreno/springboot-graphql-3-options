package com.example.api.graphql.controllers.post;

import com.example.api.model.Comment;
import com.example.api.model.Post;
import com.example.api.model.User;
import com.example.api.service.CommentService;
import com.example.api.service.UserService;
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
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostCommentsController {

    private final CommentService commentService;
    private final UserService userService;

    //@BatchMapping(typeName = "Post", field= "comments")
    @BatchMapping
    public Mono<Map<Post, List<Comment>>> comments(List<Post> posts) {
        log.info("getPostsDataloader - Getting comment for post size: {}", posts.size());
        Set<Post> postSet = new HashSet<>(posts);

        Supplier<CompletableFuture<Map<Post, List<Comment>>>> supplier = () -> commentService.getAllCommentsByPostIds(postSet);
        return Mono.fromFuture(supplier);
    }

    //    @SchemaMapping(typeName = "Post", field= "comments")
    //    List<Comment> getComments(Post post) {
    //        return commentService.getAllCommentsByPostId(post.getId());
    //    }
}
