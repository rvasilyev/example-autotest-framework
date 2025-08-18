package com.example.autotest.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Аппендер для добавления записей из системных логов в отчет Allure. Используется для настройки из конфигурационного
 * файла {@code logback.xml}.
 */
public final class AllureAppender extends AppenderBase<ILoggingEvent> {

    private static final ThreadLocal<Map<String, ILoggingEvent>> CACHE = new ThreadLocal<>();
    private Encoder<ILoggingEvent> encoder;

    /**
     * Дает доступ к кодировщику аппендера. Неявно используется для настройки из конфигурационного файла {@code logback.xml}.
     *
     * @return кодировщик аппендера
     */
    @SuppressWarnings("unused")
    public Encoder<ILoggingEvent> getEncoder() {
        return encoder;
    }

    /**
     * Задает кодировщик аппендера. Неявно используется для настройки из конфигурационного файла {@code logback.xml}.
     *
     * @param encoder кодировщик, который нужно установить для аппендера
     */
    @SuppressWarnings("unused")
    public void setEncoder(Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AllureAppender that = (AllureAppender) o;

        return encoder.equals(that.encoder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(encoder);
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        String loggerName = eventObject.getLoggerName();
        if (isStarted() && !loggerName.equals("org.springframework.cloud.openfeign.FeignClientFactoryBean")) {
            eventObject.prepareForDeferredProcessing();
            Consumer<ILoggingEvent> addAttachmentConsumer = loggingEvent -> {
                LocalDateTime loggerDateTime = LocalDateTime.ofInstant(loggingEvent.getInstant(), ZoneId.systemDefault());
                String attachmentHeader = String.format("%s %s %s", loggerDateTime, loggingEvent.getLevel(), loggerName);
                Allure.addAttachment(attachmentHeader, loggingEvent.getFormattedMessage());
            };
            if (loggerName.matches(".+Service.+Controller")) {
                processRestLoggingEvent(eventObject, addAttachmentConsumer);
            } else {
                addAttachmentConsumer.accept(eventObject);
            }
        }
    }

    private void processRestLoggingEvent(ILoggingEvent eventObject, Consumer<ILoggingEvent> addAttachmentConsumer) {
        String formattedMessage = eventObject.getFormattedMessage();
        String controllerMethodName = formattedMessage.substring(formattedMessage.indexOf("#") + 1, formattedMessage.indexOf(")") + 1);
        if(!formattedMessage.contains("Status code") && CACHE.get() == null) {
            CACHE.set(Map.of(controllerMethodName, eventObject));
        } else if (CACHE.get() != null && CACHE.get().containsKey(controllerMethodName)) {
            String controllerMethodSimpleName = controllerMethodName.substring(0, controllerMethodName.indexOf("("));
            String[] paramTypeNames = controllerMethodName.substring(controllerMethodName.indexOf("(") + 1, controllerMethodName.indexOf(")"))
                    .split(",");
            try {
                Class<?> loggerClass = Class.forName(eventObject.getLoggerName());
                Class<?>[] paramTypes = Arrays.stream(loggerClass.getMethods())
                        .filter(method -> method.getName().equals(controllerMethodSimpleName))
                        .map(Method::getParameterTypes)
                        .filter(types -> Arrays.equals(Arrays.stream(types)
                                .map(Class::getSimpleName)
                                .toArray(String[]::new), paramTypeNames))
                        .findFirst()
                        .orElseThrow();
                String stepName = loggerClass.getMethod(controllerMethodSimpleName, paramTypes).getAnnotation(Step.class).value();
                Allure.step(stepName, () -> {
                    addAttachmentConsumer.accept(CACHE.get().get(controllerMethodName));
                    addAttachmentConsumer.accept(eventObject);
                });
            } catch (Exception e) {
                addAttachmentConsumer.accept(eventObject);
            } finally {
                CACHE.remove();
            }
        } else {
            addAttachmentConsumer.accept(eventObject);
        }
    }
}
