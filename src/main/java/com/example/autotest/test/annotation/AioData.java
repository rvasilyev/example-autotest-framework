package com.example.autotest.test.annotation;

import com.example.autotest.backend.rest.jira.aio.type.AioTestAutomationStatusType;
import com.example.autotest.backend.rest.jira.aio.type.AioTestCaseType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AioData {

    String projectKey();

    String folder() default "";

    AioTestCaseType testCaseType();

    AioTestAutomationStatusType automationStatus() default AioTestAutomationStatusType.AUTOMATED;
}
