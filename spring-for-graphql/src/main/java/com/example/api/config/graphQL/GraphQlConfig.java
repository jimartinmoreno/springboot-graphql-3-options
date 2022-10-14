package com.example.api.config.graphQL;

import com.example.api.config.graphQL.directives.AuthorizationDirective;
import graphql.schema.idl.SchemaDirectiveWiring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQlConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        SchemaDirectiveWiring directiveWiring = new AuthorizationDirective();
        return wiringBuilder -> wiringBuilder
                .directiveWiring(directiveWiring)
                // .directive("auth", directiveWiring)
                .build();
    }
}
