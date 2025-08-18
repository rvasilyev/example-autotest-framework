package com.example.autotest.configuration.settings;

import com.example.autotest.exception.AutotestException;
import com.example.autotest.support.ExceptionUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Набор настроек Elasticsearch из {@code elasticsearch.properties}.
 */
public final class ElasticSearchSettings {

    /**
     * Значение настройки {@code elasticsearch.login}.
     */
    public static final String ELASTICSEARCH_LOGIN;

    /**
     * Значение настройки {@code elasticsearch.password}.
     */
    public static final String ELASTICSEARCH_PASSWORD;

    /**
     * Значение настройки {@code elasticsearch.url}.
     */
    public static final String ELASTICSEARCH_URL;

    /**
     * Значение настройки {@code elasticsearch.index.filebeat}.
     */
    public static final String ELASTICSEARCH_FILEBEAT_INDEX;

    /**
     * Значение настройки {@code elasticsearch.index.packetbeat}.
     */
    public static final String ELASTICSEARCH_PACKETBEAT_INDEX;

    static {
        try {
            Properties properties = new Properties();
            properties.load(ApplicationSettings.class.getClassLoader().getResourceAsStream("elasticsearch.properties"));
            properties.putAll(System.getProperties());
            ELASTICSEARCH_LOGIN = properties.getProperty("elasticsearch.login");
            ELASTICSEARCH_PASSWORD = properties.getProperty("elasticsearch.password");
            ELASTICSEARCH_URL = properties.getProperty("elasticsearch.url");
            ELASTICSEARCH_FILEBEAT_INDEX = properties.getProperty("elasticsearch.index.filebeat").replace("${environment.name}", ApplicationSettings.ENVIRONMENT_NAME);
            ELASTICSEARCH_PACKETBEAT_INDEX = properties.getProperty("elasticsearch.index.packetbeat").replace("${environment.name}", ApplicationSettings.ENVIRONMENT_NAME);
        } catch (IOException e) {
            throw new AutotestException(e);
        }
    }

    private ElasticSearchSettings() {
        ExceptionUtils.throwInstantiationException(getClass());
    }
}
