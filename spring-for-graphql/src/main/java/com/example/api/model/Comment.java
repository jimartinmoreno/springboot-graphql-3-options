package com.example.api.model;

import lombok.Builder;
import lombok.Value;

/**
 * Comment Data class
 */
@Value
@Builder
public class Comment {
    Long postId;
    Long id;
    String name;
    String email;
    String body;
    Commenter commenter;
}
