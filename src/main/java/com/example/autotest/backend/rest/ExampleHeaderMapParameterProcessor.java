package com.example.autotest.backend.rest;

import feign.MethodMetadata;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ExampleHeaderMapParameterProcessor implements AnnotatedParameterProcessor {

    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return ExampleHeaderMap.class;
    }

    @Override
    public boolean processArgument(AnnotatedParameterContext context, Annotation annotation, Method method) {
        int paramIndex = context.getParameterIndex();
        MethodMetadata metadata = context.getMethodMetadata();
        if (metadata.headerMapIndex() == null) {
            metadata.headerMapIndex(paramIndex);
        }
        return true;
    }
}
