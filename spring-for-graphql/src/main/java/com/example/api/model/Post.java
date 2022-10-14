package com.example.api.model;

import lombok.Builder;
import lombok.Value;

/**
 * @Value Generates a lot of code which fits with a class that is a representation of an immutable entity.
 */
@Value
@Builder
public class Post {
    Long id;
    Long userId;
    String title;
    String body;
}
