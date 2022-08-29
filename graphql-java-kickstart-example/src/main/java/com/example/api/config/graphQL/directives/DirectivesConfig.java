package com.example.api.config.graphQL.directives;

import graphql.kickstart.autoconfigure.tools.SchemaDirective;
//import graphql.kickstart.tools.boot.SchemaDirective;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectivesConfig {

    @Bean
    public SchemaDirective uppercaseDirective() {
        return new SchemaDirective("uppercase", new UppercaseDirective());
    }

    @Bean
    public SchemaDirective authorizationDirective() {
        return new SchemaDirective("auth", new AuthorizationDirective());
    }

    @Bean
    public SchemaDirective validateUserInputDirective() {
        return new SchemaDirective("validateUserInput", new ValidateUserInputDirective());
    }
}
