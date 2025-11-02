package com.example.autotest.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AioStep {

    String description() default "";

    String data() default "";

    String expectedResult() default "{expectedResult}";
}
