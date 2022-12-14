package com.example.api.graphql.resovers.post;

import com.example.api.model.Post;
import com.example.api.service.PostService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostMutationResolver implements GraphQLMutationResolver {

    private final PostService postService;

    Post createPost(Post input) {
        return postService.createPost(input);
    }

}
