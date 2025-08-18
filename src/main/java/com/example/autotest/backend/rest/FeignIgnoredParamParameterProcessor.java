package com.example.autotest.backend.rest;

import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class FeignIgnoredParamParameterProcessor implements AnnotatedParameterProcessor {

    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return FeignIgnoredParam.class;
    }

    @Override
    public boolean processArgument(AnnotatedParameterContext context, Annotation annotation, Method method) {
        return true;
    }
}
