package com.example.autotest.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для методов, представляющих собой шаги в тест-кейсах Jira AIO. Указанные в аннотации данные передаются
 * в соответствующие поля шага тест-кейса.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AioStep {

    /**
     * Описание шага тест-кейса.
     */
    String description() default "";

    /**
     * Данные шага тест-кейса. К указанным данным автоматически добавляются значения параметров аннотированного метода,
     * если они не отмечены аннотацией {@link AioIgnoredParam}.
     */
    String data() default "";

    /**
     * Описание ожидаемого результата шага тест-кейса. Поддерживается подстановка значений параметров:
     * <ul>
     *     <li>{имяПараметра} подставит значение соответствующего параметра, переведенное в строку</li>
     *     <li>{this} подставит строковое представление объекта, в котором вызывается аннотированный метод</li>
     *     <li>{полное квалифицированное имя} подставит пример строкового представления объекта данного типа по схеме {@link com.example.autotest.support.AioUtils#createObject(String)}</li>
     * </ul>
     * Значение по умолчанию - "{expectedResult}".
     */
    String expectedResult() default "{expectedResult}";
}
