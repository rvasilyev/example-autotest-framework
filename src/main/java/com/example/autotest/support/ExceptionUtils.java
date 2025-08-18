package com.example.autotest.support;

public final class ExceptionUtils {

    private ExceptionUtils() {
        throwInstantiationException(getClass());
    }

    public static void throwInstantiationException(Class<?> clazz) {
        throw new IllegalStateException("Попытка создать экземпляр неинстанцируемого класса " + clazz);
    }
}
