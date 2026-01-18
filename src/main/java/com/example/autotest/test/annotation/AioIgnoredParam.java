package com.example.autotest.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для параметров методов, аннотированных {@link AioStep}, исключающая добавление их значений в раздел данных
 * шага тест-кейса.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AioIgnoredParam {
}
