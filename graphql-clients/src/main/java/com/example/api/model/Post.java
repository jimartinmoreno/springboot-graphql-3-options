package com.example.api.model;

import lombok.ToString;
import lombok.Value;

/**
 * @Value Generates a lot of code which fits with a class that is a representation of an immutable entity.
 */
@Value
@ToString
public class Post {
    Long id;
    Long userId;
    String title;
    String body;
}
