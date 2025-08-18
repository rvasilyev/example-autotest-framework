package com.example.autotest.configuration.settings;

import com.example.autotest.exception.AutotestException;
import com.example.autotest.support.ExceptionUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Набор настроек Allure из {@code allure.properties}.
 */
public final class AllureSettings {

    /**
     * Значение настройки {@code allure.results.directory}.
     */
    public static final String RESULTS_DIRECTORY;

    /**
     * Значение настройки {@code allure.link.issue.pattern}.
     */
    public static final String ISSUE_LINK_PATTERN;

    /**
     * Значение настройки {@code allure.link.tms.pattern}.
     */
    public static final String TMS_LINK_PATTERN;

    static {
        try {
            Properties properties = new Properties();
            properties.load(AllureSettings.class.getClassLoader().getResourceAsStream("allure.properties"));
            properties.putAll(System.getProperties());
            RESULTS_DIRECTORY = properties.getProperty("allure.results.directory");
            ISSUE_LINK_PATTERN = properties.getProperty("allure.link.issue.pattern");
            TMS_LINK_PATTERN = properties.getProperty("allure.link.tms.pattern");
        } catch (IOException e) {
            throw new AutotestException(e);
        }
    }

    private AllureSettings() {
        ExceptionUtils.throwInstantiationException(getClass());
    }
}
