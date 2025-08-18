package com.example.autotest.support.frontend;

import com.codeborne.selenide.WebDriverRunner;
import com.example.autotest.configuration.settings.BrowserSettings;
import com.example.autotest.exception.AutotestException;
import com.example.autotest.frontend.webdriver.WebDriverType;
import com.example.autotest.support.ExceptionUtils;
import com.example.autotest.test.TestResult;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.RecordingFileFactory;
import org.testcontainers.containers.SocatContainer;
import org.testcontainers.containers.startupcheck.IsRunningStartupCheckStrategy;
import org.testcontainers.lifecycle.TestDescription;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Потокобезопасный менеджер управления веб-драйвером с предоставлением некоторых дополнительных функций.
 */
public final class WebDriverUtils {

    private static final Logger LOG = LoggerFactory.getLogger(WebDriverUtils.class);

    private static final ThreadLocal<Network> localNetwork = new ThreadLocal<>();
    private static final ThreadLocal<SocatContainer> localSocatContainer = new ThreadLocal<>();
    private static final ThreadLocal<BrowserWebDriverContainer<?>> localWebDriverContainer = new ThreadLocal<>();

    private WebDriverUtils() {
        ExceptionUtils.throwInstantiationException(getClass());
    }

    /**
     * Создает новый экземпляр веб-драйвера и передает управление им библиотеке {@code Selenide}.
     */
    public static void createWebDriver() {
        Capabilities capabilities = createCapabilities();
        DriverManagerType driverManagerType = switch (BrowserSettings.BROWSER_TYPE) {
            case CHROME -> DriverManagerType.CHROME;
            case FIREFOX -> DriverManagerType.FIREFOX;
            case EDGE -> DriverManagerType.EDGE;
            case INTERNET_EXPLORER -> DriverManagerType.IEXPLORER;
        };
        WebDriverManager webDriverManager = WebDriverManager.getInstance(driverManagerType).capabilities(capabilities);

        WebDriver webDriver;
        if (WebDriverType.TEST_CONTAINERS.equals(BrowserSettings.WEB_DRIVER_TYPE)) {
            runContainers(capabilities);
            webDriver = webDriverManager.remoteAddress(localWebDriverContainer.get().getSeleniumAddress()).create();
            if (webDriver instanceof RemoteWebDriver remoteWebDriver) {
                remoteWebDriver.setFileDetector(new LocalFileDetector());
            }
        } else {
            webDriver = webDriverManager.create();
        }

        WebDriverRunner.setWebDriver(webDriver);

        LOG.info("Запущен веб-драйвер {}", webDriver);
    }

    /**
     * Закрывает используемый экземпляр веб-драйвера, если он запущен, иначе не делает ничего.
     */
    public static void closeWebDriver() {
        WebDriverRunner.closeWebDriver();
        LOG.info("Веб-драйвер остановлен");

        Consumer<? super GenericContainer<?>> containerConsumer = container -> {
            container.stop();
            container.close();
            if (container instanceof BrowserWebDriverContainer) {
                LOG.info("Остановлен Docker-контейнер для веб-драйвера");
            }
        };
        Optional.ofNullable(localWebDriverContainer.get()).ifPresent(containerConsumer);
        Optional.ofNullable(localSocatContainer.get()).ifPresent(containerConsumer);
        Optional.ofNullable(localNetwork.get()).ifPresent(Network::close);

        localWebDriverContainer.remove();
        localSocatContainer.remove();
        localNetwork.remove();
    }

    /**
     * Сохраняет записанное видео прохождения теста, если запись видео включена в {@link BrowserSettings}.
     *
     * @param testId                 идентификатор теста
     * @param testName               название теста
     * @param testExecutionThrowable ошибка при выполнении теста ({@code null}, если тест пройден без ошибок)
     * @return абсолютный путь до видео-файла или {@code null}, если видео-файл не может быть создан
     */
    public static Path saveTestVideo(String testId, String testName, Throwable testExecutionThrowable) {
        if (WebDriverType.TEST_CONTAINERS.equals(BrowserSettings.WEB_DRIVER_TYPE)) {
            TestResult testResult;
            if (testExecutionThrowable != null) {
                if (testExecutionThrowable instanceof AssertionError) {
                    testResult = TestResult.FAILED;
                } else {
                    testResult = TestResult.BROKEN;
                }
            } else {
                testResult = TestResult.PASSED;
            }

            String normalizedScenarioName = testName.replaceAll("[.,:;?!+#№%&*|\\s/\\\\]", "_");
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
            String videoName = String.format("%s-%s-%s", normalizedScenarioName, testResult, date);
            AtomicReference<Path> videoPath = new AtomicReference<>();

            RecordingFileFactory recordingFileFactory = (File vncRecordingDirectory, String prefix, boolean succeeded) -> {
                File videoFile = new File(vncRecordingDirectory, String.format("%s.%s", videoName, BrowserSettings.WEB_DRIVER_VIDEO_FORMAT.getFilenameExtension()));
                videoPath.set(videoFile.toPath());
                return videoFile;
            };
            localWebDriverContainer.get().withRecordingFileFactory(recordingFileFactory);

            TestDescription testDescription = createTestDescription(testId, normalizedScenarioName);
            Optional<Throwable> optionalThrowable;
            if (testResult.equals(TestResult.PASSED)) {
                optionalThrowable = Optional.empty();
            } else {
                optionalThrowable = Optional.of(new AutotestException(String.format("%s %s", testName, testResult.name().toLowerCase())));
            }
            localWebDriverContainer.get().afterTest(testDescription, optionalThrowable);

            Path absoluteVideoPath = videoPath.get().toAbsolutePath();
            LOG.info("Сохранена видеозапись теста '{}' по пути '{}'", testName, absoluteVideoPath);
            return absoluteVideoPath;
        } else {
            LOG.warn("Невозможно сохранить видеозапись теста. Запись видео поддерживается только для удаленного вэб-драйвера");
            return null;
        }
    }

    /**
     * Предоставляет текущие логи запущенного веб-драйвера.
     *
     * @return текущие логи запущенного веб-драйвера
     * @throws AutotestException, если экземпляр веб-драйвера не существует
     */
    public static List<LogEntry> getBrowserLogs() {
        return WebDriverRunner.getWebDriver()
                .manage()
                .logs()
                .get(LogType.BROWSER)
                .getAll();
    }

    /**
     * Делает снимок текущей страницы браузера, связанного с запущенным драйвером, в заданной форме.
     *
     * @param outputType форма создаваемого снимка
     * @param <T>        параметр типа возвращаемого значения (формы снимка)
     * @return снимок текущей страницы браузера в заданной форме
     * @throws AutotestException, если экземпляр веб-драйвера не существует
     */
    public static <T> T takeScreenshot(OutputType<T> outputType) {
        T screenshot = ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(outputType);
        LOG.info("Сделан снимок текущей страницы браузера");
        return screenshot;
    }

    private static Capabilities createCapabilities() {
        MutableCapabilities capabilities = switch (BrowserSettings.BROWSER_TYPE) {
            case CHROME -> {
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--enable-automation");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-infobars");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-browser-side-navigation");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--use-fake-ui-for-media-stream");
                chromeOptions.addArguments("--use-fake-device-for-media-stream");
                chromeOptions.addArguments("--ignore-certificate-errors");
                Map<String, Object> chromePrefs = Map.of(
                        "profile.default_content_settings.popups", 0,
                        "download.default_directory", BrowserSettings.WEB_DRIVER_DOWNLOAD_DIRECTORY.toString(),
                        "download.directory_upgrade", true
                );
                chromeOptions.setExperimentalOption("prefs", chromePrefs);
                chromeOptions.setEnableDownloads(true);
                yield chromeOptions;
            }
            case FIREFOX -> new FirefoxOptions();
            case EDGE -> new EdgeOptions();
            case INTERNET_EXPLORER -> new InternetExplorerOptions();
        };

        Map<String, Object> checkedOptions = Optional.ofNullable(BrowserSettings.BROWSER_OPTIONS).orElse(Collections.emptyMap());
        checkedOptions.forEach(capabilities::setCapability);
        LOG.info("Созданы настройки драйвера: {}", capabilities);
        return capabilities;
    }

    @SuppressWarnings("resource")
    private static void runContainers(Capabilities capabilities) {
        localNetwork.set(Network.newNetwork());
        String browserType = BrowserSettings.BROWSER_TYPE.name().toLowerCase();

        SocatContainer socatContainer = new SocatContainer()
                .withTarget(2000, browserType, 4444)
                .withNetwork(localNetwork.get());
        localSocatContainer.set(socatContainer);
        localSocatContainer.get().start();

        BrowserWebDriverContainer.VncRecordingMode recordingMode = BrowserSettings.WEB_DRIVER_VIDEO_RECORDING;

        Path videoDirectory = BrowserSettings.WEB_DRIVER_VIDEO_DIRECTORY;
        if (!recordingMode.equals(BrowserWebDriverContainer.VncRecordingMode.SKIP) && Files.notExists(videoDirectory)) {
            try {
                Files.createDirectories(videoDirectory);
            } catch (IOException e) {
                throw new AutotestException(e);
            }
        }

        BrowserWebDriverContainer<?> browserWebDriverContainer = new BrowserWebDriverContainer<>()
                .withEnv("SE_NODE_GRID_URL", String.format("http://%s:%d", localSocatContainer.get().getHost(), localSocatContainer.get().getMappedPort(2000)))
                .withNetworkAliases(browserType)
                .withNetwork(localNetwork.get())
                .withCapabilities(capabilities)
                .withRecordingMode(recordingMode, videoDirectory.toFile(), BrowserSettings.WEB_DRIVER_VIDEO_FORMAT)
                .withStartupCheckStrategy(new IsRunningStartupCheckStrategy());
        localWebDriverContainer.set(browserWebDriverContainer);
        browserWebDriverContainer.start();

        LOG.info("Запущен Docker-контейнер для веб-драйвера");
    }

    private static TestDescription createTestDescription(String testId, String filesystemFriendlyName) {
        return new TestDescription() {
            @Override
            public String getTestId() {
                return testId;
            }

            @Override
            public String getFilesystemFriendlyName() {
                return filesystemFriendlyName;
            }
        };
    }
}
