package com.example.api.config.graphQL;

import com.example.api.config.graphQL.directives.AuthorizationDirective;
import com.example.api.config.graphQL.directives.UppercaseDirective;
import graphql.schema.idl.SchemaDirectiveWiring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQlConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .directive("auth", new AuthorizationDirective())
                .directive("uppercase", new UppercaseDirective())
                .build();
//                .directiveWiring(new AuthorizationDirective());

    }
}
