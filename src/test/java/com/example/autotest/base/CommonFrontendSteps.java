package com.example.autotest.base;

import io.qameta.allure.Step;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import com.example.autotest.frontend.element.common.Loader;
import com.example.autotest.support.ExceptionUtils;

import java.util.concurrent.TimeUnit;

public final class CommonFrontendSteps {

    private CommonFrontendSteps() {
        ExceptionUtils.throwInstantiationException(getClass());
    }

    @Step("Ожидание состояния индикатора загрузки")
    public static void waitForLoaderState(Loader loader, boolean mustExist) {
        Awaitility.given()
                .pollInSameThread()
                .pollDelay(1, TimeUnit.SECONDS)
                .timeout(30, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .until(() -> mustExist == loader.isExist());
    }
}
