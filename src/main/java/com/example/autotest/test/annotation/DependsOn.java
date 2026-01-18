package com.example.autotest.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для зависимых <b>тестов</b>, содержащая массив имен методов, от которых зависит данный тест. Имя метода должно
 * представлять собой полное квалифицированное имя с указаннием типов параметров в круглых скобках.
 * Поддерживается указание методов, находящихся в других классах. Пример:
 * <ul>
 *     <li>testMethod1() выберет метод в том же классе, что и зависимый тест, с именем testMethod1 и пустым списком параметров</li>
 *     <li>testMethod1(int,String) выберет метод в том же классе, что и зависимый тест, с именем testMethod1 и параметрами типа {@code int} и {@code String} в таком же порядке</li>
 *     <li>com.package.MyClass#testMethod1() выберет метод в классе com.package.MyClass с именем testMethod1 и пустым списком параметров</li>
 *     <li>и т.д.</li>
 * </ul>
 * Для массивов помимо кодового стиля вида {@code int[], double[][]} также допускается использование внутренних строковых
 * представлений вида {@code [I, [[D}.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DependsOn {

    /**
     * Массив имен тестовых методов, от которых зависит данный тест.
     */
    String[] value();
}
