package com.example.autotest.configuration.settings;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.VncRecordingContainer;
import com.example.autotest.exception.AutotestException;
import com.example.autotest.frontend.webdriver.BrowserType;
import com.example.autotest.frontend.webdriver.WebDriverType;
import com.example.autotest.support.ExceptionUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * Набор настроек приложения из {@code application.properties}.
 */
public final class BrowserSettings {

    /**
     * Значение настройки {@code browser.type}.
     */
    public static final BrowserType BROWSER_TYPE;

    /**
     * Значение настройки {@code browser.options} в форме {@code Map}, доступной только для чтения.
     */
    public static final Map<String, Object> BROWSER_OPTIONS;

    /**
     * Значение настройки {@code webdriver.type}.
     */
    public static final WebDriverType WEB_DRIVER_TYPE;

    /**
     * Значение настройки {@code webdriver.video.recording}.
     */
    public static final BrowserWebDriverContainer.VncRecordingMode WEB_DRIVER_VIDEO_RECORDING;

    /**
     * Значение настройки {@code webdriver.video.format}.
     */
    public static final VncRecordingContainer.VncRecordingFormat WEB_DRIVER_VIDEO_FORMAT;

    /**
     * Значение настройки {@code webdriver.video.directory}.
     */
    public static final Path WEB_DRIVER_VIDEO_DIRECTORY;

    /**
     * Значение настройки {@code webdriver.download.directory}.
     */
    public static final Path WEB_DRIVER_DOWNLOAD_DIRECTORY;

    static {
        try {
            Properties properties = new Properties();
            properties.load(BrowserSettings.class.getClassLoader().getResourceAsStream("browser.properties"));
            properties.putAll(System.getProperties());
            String browserType = properties.getProperty("browser.type");
            BROWSER_TYPE = convertStringToEnum(BrowserType.class, browserType, String.format("Браузер '%s' не поддерживается", browserType));
            BROWSER_OPTIONS = Collections.unmodifiableMap(new ObjectMapper().readValue(properties.getProperty("browser.options"), new TypeReference<>() {
            }));
            String webDriverType = properties.getProperty("webdriver.type");
            WEB_DRIVER_TYPE = convertStringToEnum(WebDriverType.class, webDriverType, String.format("Тип веб-драйвера '%s' не поддерживается", webDriverType));
            String webDriverVideoRecording = properties.getProperty("webdriver.video.recording");
            WEB_DRIVER_VIDEO_RECORDING = convertStringToEnum(BrowserWebDriverContainer.VncRecordingMode.class, webDriverVideoRecording, String.format("Режим видеозаписи '%s' не поддерживается", webDriverVideoRecording));
            String webDriverVideoFormat = properties.getProperty("webdriver.video.format");
            WEB_DRIVER_VIDEO_FORMAT = convertStringToEnum(VncRecordingContainer.VncRecordingFormat.class, webDriverVideoFormat, String.format("Формат видеозаписи '%s' не поддерживается", webDriverVideoFormat));
            WEB_DRIVER_VIDEO_DIRECTORY = Path.of(properties.getProperty("webdriver.video.directory"));
            WEB_DRIVER_DOWNLOAD_DIRECTORY = Path.of(properties.getProperty("webdriver.download.directory"));
        } catch (Exception e) {
            throw new AutotestException(e);
        }
    }

    private BrowserSettings() {
        ExceptionUtils.throwInstantiationException(getClass());
    }

    private static <T extends Enum<T>> T convertStringToEnum(Class<T> enumClass, String stringValue, String errorMessage) {
        try {
            String convertedString = stringValue.replace(" ", "_").toUpperCase();
            return Enum.valueOf(enumClass, convertedString);
        } catch (IllegalArgumentException e) {
            throw new AutotestException(errorMessage);
        }
    }
}
