package com.example.autotest.configuration.settings;

import com.example.autotest.exception.AutotestException;
import com.example.autotest.support.ExceptionUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Набор настроек приложения из {@code application.properties}.
 */
public final class ApplicationSettings {

    /**
     * Значение настройки {@code environment.name}.
     */
    public static final String ENVIRONMENT_NAME;

    /**
     * Значение настройки {@code environment.url} с подставленным значением {@code environment.name}.
     */
    public static final String ENVIRONMENT_URL;

    /**
     * Значение настройки {@code frontend.wait.timeout} в миллисекундах.
     */
    public static final long FRONTEND_WAIT_TIMEOUT;

    /**
     * Значение настройки {@code frontend.wait.interval} в миллисекундах.
     */
    public static final long FRONTEND_WAIT_INTERVAL;

    /**
     * Значение настройки {@code dependent.test.wait.timeout} в секундах.
     */
    public static final long DEPENDENT_TEST_WAIT_TIMEOUT;

    /**
     * Значение настройки {@code dependent.test.wait.interval} в секундах.
     */
    public static final long DEPENDENT_TEST_WAIT_INTERVAL;

    static {
        try {
            Properties properties = new Properties();
            properties.load(ApplicationSettings.class.getClassLoader().getResourceAsStream("application.properties"));
            properties.putAll(System.getProperties());
            ENVIRONMENT_NAME = properties.getProperty("environment.name");
            ENVIRONMENT_URL = properties.getProperty("environment.url").replace("${environment.name}", ENVIRONMENT_NAME);
            FRONTEND_WAIT_TIMEOUT = Long.parseLong(properties.getProperty("frontend.wait.timeout"));
            FRONTEND_WAIT_INTERVAL = Long.parseLong(properties.getProperty("frontend.wait.interval"));
            DEPENDENT_TEST_WAIT_TIMEOUT = Long.parseLong(properties.getProperty("dependent.test.wait.timeout"));
            DEPENDENT_TEST_WAIT_INTERVAL = Long.parseLong(properties.getProperty("dependent.test.wait.interval"));
        } catch (IOException e) {
            throw new AutotestException(e);
        }
    }

    private ApplicationSettings() {
        ExceptionUtils.throwInstantiationException(getClass());
    }
}
