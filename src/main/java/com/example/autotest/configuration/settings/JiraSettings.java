package com.example.autotest.configuration.settings;

import com.example.autotest.exception.AutotestException;
import com.example.autotest.support.ExceptionUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Набор настроек для интеграции с Jira из {@code jira.properties}.
 */
public final class JiraSettings {

    /**
     * Значение настройки {@code jira.url}.
     */
    public static final String JIRA_API_URL;

    /**
     * Значение настройки {@code jira.login}.
     */
    public static final String JIRA_LOGIN;

    /**
     * Значение настройки {@code jira.password}.
     */
    public static final String JIRA_PASSWORD;

    /**
     * Значение настройки {@code aio.case.creation}.
     */
    public static final boolean AIO_CASE_CREATION;

    /**
     * Значение настройки {@code force.aio.case.creation}.
     */
    public static final boolean FORCE_AIO_CASE_CREATION;

    static {
        try {
            Properties properties = new Properties();
            properties.load(JiraSettings.class.getClassLoader().getResourceAsStream("jira.properties"));
            properties.putAll(System.getProperties());
            JIRA_API_URL = properties.getProperty("jira.api.url");
            JIRA_LOGIN = properties.getProperty("jira.login");
            JIRA_PASSWORD = properties.getProperty("jira.password");
            AIO_CASE_CREATION = Boolean.parseBoolean(properties.getProperty("aio.case.creation"));
            FORCE_AIO_CASE_CREATION = Boolean.parseBoolean(properties.getProperty("force.aio.case.creation"));
        } catch (IOException e) {
            throw new AutotestException(e);
        }
    }

    private JiraSettings() {
        ExceptionUtils.throwInstantiationException(getClass());
    }
}
