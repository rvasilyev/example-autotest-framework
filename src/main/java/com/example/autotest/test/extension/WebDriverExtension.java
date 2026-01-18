package com.example.autotest.test.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.junitplatform.AllureJunitPlatform;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.autotest.configuration.settings.BrowserSettings;
import com.example.autotest.exception.AutotestException;
import com.example.autotest.support.ExtensionUtils;
import com.example.autotest.support.frontend.WebDriverUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Расширение для JUnit, позволяющее автоматически создавать и уничтожать веб-драйвер для запускаемого теста в соответствии
 * с настройками.
 * @see WebDriverUtils
 * @see BrowserSettings
 */
public final class WebDriverExtension implements BeforeEachCallback, AfterEachCallback {

    private static final Logger LOG = LoggerFactory.getLogger(WebDriverExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) {
        try {
            ExtensionUtils.processFixture(AllureJunitPlatform.PREPARE, WebDriverUtils::createWebDriver, context);
        } catch (WebDriverException e) {
            WebDriverUtils.closeWebDriver();
            throw new AutotestException(e);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Runnable runnable = () -> {
            try {
                Throwable testExecutionThrowable = context.getExecutionException().orElse(null);
                attachVideo(context.getUniqueId(), context.getDisplayName(), testExecutionThrowable);
                if (testExecutionThrowable != null) {
                    try {
                        Path screenshot = WebDriverUtils.takeScreenshot(OutputType.FILE).toPath();
                        Allure.addAttachment("Снимок места падения", "image/png", Files.newInputStream(screenshot), "png");
                    } catch (Exception e) {
                        LOG.warn("Не удалось сделать снимок при падении теста:\n{}", e.getMessage());
                    }

                    try {
                        Function<LogEntry, String> mapFunction = logEntry -> {
                            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(logEntry.getTimestamp()), ZoneId.systemDefault());
                            return String.format("%s\t%s\t%s", localDateTime, logEntry.getLevel(), logEntry.getMessage());
                        };
                        String browserLog = WebDriverUtils.getBrowserLogs().stream()
                                .map(mapFunction)
                                .collect(Collectors.joining("\n"));
                        Allure.addAttachment("Лог браузера", "text/plain", browserLog);
                    } catch (Exception e) {
                        LOG.warn("Не удалось получить лог браузера:\n{}", e.getMessage());
                    }
                }
            } finally {
                WebDriverUtils.closeWebDriver();
            }
        };
        ExtensionUtils.processFixture(AllureJunitPlatform.TEAR_DOWN, runnable, context);
    }

    private void attachVideo(String testId, String testName, Throwable testExecutionThrowable) {
        Path recordingDirectory = BrowserSettings.WEB_DRIVER_VIDEO_DIRECTORY;
        Path videoPath = WebDriverUtils.saveTestVideo(testId, testName, testExecutionThrowable);
        if (Files.exists(recordingDirectory)) {
            try (Stream<Path> videos = Files.walk(recordingDirectory)) {
                videos.filter(path -> path.toAbsolutePath().equals(videoPath))
                        .findFirst()
                        .ifPresent(path -> {
                            try {
                                Allure.addAttachment(path.toFile().getName(), Files.newInputStream(path));
                            } catch (Exception e) {
                                LOG.warn("Не удалось прикрепить видеозапись прохождения теста:\n{}", e.getMessage());
                            }
                        });
            } catch (Exception e) {
                LOG.warn("Не удалось получить список видеозаписей прохождения тестов:\n{}", e.getMessage());
            }
        }
    }
}
