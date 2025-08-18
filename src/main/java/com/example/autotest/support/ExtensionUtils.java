package com.example.autotest.support;

import io.qameta.allure.junitplatform.AllureJunitPlatform;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.util.ResultsUtils;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class ExtensionUtils {

    private ExtensionUtils() {
        ExceptionUtils.throwInstantiationException(getClass());
    }

    public static void processFixture(String fixtureType, Runnable runnable, ExtensionContext extensionContext) {
        String uuid = UUID.randomUUID().toString();
        try {
            String methodName = extensionContext.getRequiredTestMethod().getName();
            extensionContext.publishReportEntry(buildStartEvent(fixtureType, uuid, methodName));
            runnable.run();
            extensionContext.publishReportEntry(buildStopEvent(fixtureType, uuid));
        } catch (Exception e) {
            extensionContext.publishReportEntry(buildFailureEvent(fixtureType, uuid, e));
            throw e;
        }
    }

    private static Map<String, String> buildStartEvent(String type, String uuid, String methodName) {
        Map<String, String> map = prepareEvent(type, AllureJunitPlatform.EVENT_START, uuid);
        map.put("name", methodName);
        return map;
    }

    private static Map<String, String> buildStopEvent(String type, String uuid) {
        return prepareEvent(type, AllureJunitPlatform.EVENT_STOP, uuid);
    }

    private static Map<String, String> buildFailureEvent(String type, String uuid, Throwable throwable) {
        Map<String, String> map = prepareEvent(type, AllureJunitPlatform.EVENT_FAILURE, uuid);

        ResultsUtils.getStatus(throwable).map(Status::value).ifPresent(status -> map.put("status", status));

        Optional<StatusDetails> maybeDetails = ResultsUtils.getStatusDetails(throwable);
        maybeDetails.map(StatusDetails::getMessage).ifPresent(message -> map.put("message", message));
        maybeDetails.map(StatusDetails::getTrace).ifPresent(trace -> map.put("trace", trace));

        map.forEach((key, value) -> {
                    if (Objects.isNull(value) || value.trim().isEmpty()) {
                        map.replace(key, AllureJunitPlatform.ALLURE_REPORT_ENTRY_BLANK_PREFIX + value);
                    }
                }
        );

        return map;
    }

    private static Map<String, String> prepareEvent(String fixtureType, String eventType, String uuid) {
        Map<String, String> map = new HashMap<>();
        map.put(AllureJunitPlatform.ALLURE_FIXTURE, fixtureType);
        map.put("event", eventType);
        map.put("uuid", uuid);
        return map;
    }
}
