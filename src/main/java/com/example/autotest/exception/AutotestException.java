package com.example.autotest.exception;

/**
 * Исключение-обертка для других исключений и специфических ошибок автотестирования.
 */
public final class AutotestException extends RuntimeException {

    /**
     * Создает новый объект {@code AutotestException} с заданным описанием ошибки.
     *
     * @param message описание ошибки
     */
    public AutotestException(String message) {
        super(message);
    }

    /**
     * Создает новый объект {@code AutotestException} с заданным описанием ошибки и внешней причиной.
     *
     * @param message описание ошибки
     * @param cause   внешняя причина исключения
     */
    public AutotestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает новый объект {@code AutotestException} с заданной внешней причиной.
     *
     * @param cause внешняя причина исключения
     */
    public AutotestException(Throwable cause) {
        super(cause);
    }
}
