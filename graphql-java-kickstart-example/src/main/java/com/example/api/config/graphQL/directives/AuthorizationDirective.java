package com.example.api.config.graphQL.directives;

import graphql.GraphQLContext;
import graphql.language.StringValue;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.api.graphql.instrumentation.RequestLoggingInstrumentation.SECURITY_CONTEXT;

@Slf4j
public class AuthorizationDirective implements SchemaDirectiveWiring {

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        String targetAuthRole = ((StringValue) Objects.requireNonNull(environment.getAppliedDirective()
                .getArgument("role")
                .getArgumentValue().getValue())
        ).getValue();

        GraphQLFieldDefinition field = environment.getElement();
        GraphQLFieldsContainer parentType = environment.getFieldsContainer();

        //1.  Validate that all the specified roles are correct according to the Roles ENUM if don't, throws a new exception
        log.info("onField -> Field: {}, ParentType: {}, Role: {}", field.getName(), parentType.getName(), targetAuthRole);

        // build a data fetcher that first checks authorisation roles before then calling the original data fetcher
        DataFetcher<?> originalDataFetcher = environment.getCodeRegistry().getDataFetcher(parentType, field);

        DataFetcher<?> authDataFetcher = dataFetchingEnvironment -> {

            GraphQLContext graphQlContext = dataFetchingEnvironment.getGraphQlContext();
            log.info("onField: {},  GraphQlContext: {}", field.getName(), graphQlContext);
            SecurityContext securityContext = graphQlContext.get(SECURITY_CONTEXT);
            SecurityContextHolder.setContext(securityContext);
//            MDC.put(CORRELATION_ID, graphQlContext.get(CORRELATION_ID));

            if (hasRole(targetAuthRole)) {
                return originalDataFetcher.get(dataFetchingEnvironment);
            } else {
                //2. Throws the same exception as the one currently thrown
                throw new IllegalAccessException("No access permission");
            }
        };

        // now change the field definition to have the new authorising data fetcher
        environment.getCodeRegistry().dataFetcher(parentType, field, authDataFetcher);
        return field;
    }

    private boolean hasRole(String targetAuthRole) {
        //3. Get the security context from the graphQl Context
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return Optional.ofNullable(authentication)
                .map(auth -> {
                    log.info("hasRole >>>>>>>>> AUTHORITIES: {}", authentication.getAuthorities());
                    List<String> roles = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority).toList();
                    return roles.contains(targetAuthRole);
                })
                .orElse(false);
    }
}
