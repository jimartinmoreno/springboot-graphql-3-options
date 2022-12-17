package com.example.api.config.graphQL.accessor;

import lombok.extern.slf4j.Slf4j;
//import org.springframework.graphql.execution.ThreadLocalAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;

@Slf4j
//@Component
public class RequestAttributesAccessor{ //implements ThreadLocalAccessor {

    private static final String KEY = RequestAttributesAccessor.class.getName();

//    @Override
    public void extractValues(Map<String, Object> container) {
        log.info("extractValues -> container: {}", container);
        //        log.info("extractValues -> RequestAttributes: {}", RequestContextHolder.getRequestAttributes().getAttributeNames(RequestAttributes.SCOPE_REQUEST));
        //        log.info("extractValues -> CurrentRequestAttributes: {}", RequestContextHolder.currentRequestAttributes().getAttributeNames(RequestAttributes.SCOPE_REQUEST));
        container.put(KEY, RequestContextHolder.getRequestAttributes());
    }

//    @Override
    public void restoreValues(Map<String, Object> values) {
        log.info("restoreValues -> values: {}", values);
        //        log.info("restoreValues -> RequestAttributes: {}", RequestContextHolder.getRequestAttributes());
        if (values.containsKey(KEY)) {
            RequestContextHolder.setRequestAttributes((RequestAttributes) values.get(KEY));
        }
        log.info("restoreValues -> RequestAttributes 2: {}", RequestContextHolder.getRequestAttributes());
    }

//    @Override
    public void resetValues(Map<String, Object> values) {
        log.info("resetValues -> values: {}", values);
        //        log.info("resetValues -> RequestAttributes: {}", RequestContextHolder.getRequestAttributes());
        RequestContextHolder.resetRequestAttributes();
        log.info("resetValues -> RequestAttributes: {}", RequestContextHolder.getRequestAttributes());
    }
}
