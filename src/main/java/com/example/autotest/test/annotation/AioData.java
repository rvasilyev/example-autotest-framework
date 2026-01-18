package com.example.autotest.test.annotation;

import com.example.autotest.backend.rest.jira.aio.type.AioTestAutomationStatusType;
import com.example.autotest.backend.rest.jira.aio.type.AioTestCaseType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для методов, представляющих собой тест-кейсы Jira AIO. Указанные в аннотации данные передаются
 * в соответствующие поля тест-кейса (кроме ключа проекта).
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AioData {

    /**
     * Ключ (id) проекта в Jira.
     */
    String projectKey();

    /**
     * Директория тест-кейса в Jira AIO.
     */
    String folder() default "";

    /**
     * Тип тест-кейса в Jira AIO.
     * @see AioTestCaseType
     */
    AioTestCaseType testCaseType();

    /**
     * Статус автоматизации тест-кейса в Jira AIO. Значение по умолчанию - {@link AioTestAutomationStatusType#AUTOMATED}
     * @see AioTestAutomationStatusType
     */
    AioTestAutomationStatusType automationStatus() default AioTestAutomationStatusType.AUTOMATED;
}
