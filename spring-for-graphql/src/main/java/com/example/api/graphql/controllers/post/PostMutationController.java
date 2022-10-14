package com.example.api.graphql.controllers.post;

import com.example.api.model.Post;
import com.example.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostMutationController {

    private final PostService postService;

    @MutationMapping
    Post createPost(@Argument Post input) {
        return postService.createPost(input);
    }
}
