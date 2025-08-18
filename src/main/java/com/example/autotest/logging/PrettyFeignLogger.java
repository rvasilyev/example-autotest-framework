package com.example.autotest.logging;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import org.slf4j.LoggerFactory;
import com.example.autotest.exception.AutotestException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class PrettyFeignLogger extends Logger {

    private final org.slf4j.Logger log;
    private final ObjectMapper objectMapper;

    public PrettyFeignLogger(Class<?> classToLog) {
        super();
        log = LoggerFactory.getLogger(classToLog);
        objectMapper = new ObjectMapper()
                .findAndRegisterModules()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        if (logLevel.ordinal() >= Level.BASIC.ordinal()) {
            String protocolVersion = Logger.resolveProtocolVersion(request.protocolVersion());
            String basicInfo = String.format(
                    "API: %s%nProtocol: %s%nMethod: %s%nURL: %s%n",
                    configKey,
                    protocolVersion,
                    request.httpMethod(),
                    request.url()
            );
            StringBuilder sb = new StringBuilder(basicInfo);

            if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {
                writeHeaders(request.headers(), sb);

                if (logLevel.ordinal() >= Level.FULL.ordinal()) {
                    writeParameters(request, sb);
                    writeBody(request.body(), sb);
                }
            }

            log.info("Отправлен запрос:\n{}", sb);
        }
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        Response result = response;
        if (logLevel.ordinal() >= Level.BASIC.ordinal()) {
            String protocolVersion = Logger.resolveProtocolVersion(response.protocolVersion());
            String basicInfo = String.format(
                    "API: %s%nProtocol: %s%nStatus code: %s%nReason: %s%nElapsed time: %d ms%n",
                    configKey,
                    protocolVersion,
                    response.status(),
                    response.reason(),
                    elapsedTime
            );
            StringBuilder sb = new StringBuilder(basicInfo);

            if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {
                writeHeaders(response.headers(), sb);

                if (logLevel.ordinal() >= Level.FULL.ordinal() && response.body() != null) {
                    byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                    if (bodyData.length > 0) {
                        writeBody(bodyData, sb);
                    }
                    //После получения данных тела поток ввода закрывается, поэтому пересоздаем тело, иначе будут ошибки
                    result = response.toBuilder().body(bodyData).build();
                }
            }

            log.info("Получен ответ:\n{}", sb);
        }

        return result;
    }

    @Override
    protected void logRetry(String configKey, Level logLevel) {
        if (logLevel.ordinal() >= Level.BASIC.ordinal()) {
            log.info("Повторный вызов {} ...", configKey);
        }
    }

    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        if (logLevel.ordinal() >= Level.BASIC.ordinal()) {
            String basicInfo = String.format("API:%s%nElapsed time:%d ms%nMessage:%s%n", configKey, elapsedTime, ioe.getMessage());
            StringBuilder sb = new StringBuilder(basicInfo);

            if (logLevel.ordinal() >= Level.FULL.ordinal()) {
                try (StringWriter sw = new StringWriter()) {
                    ioe.printStackTrace(new PrintWriter(sw));
                    sb.append(sw);
                } catch (IOException e) {
                    throw new AutotestException(e);
                }
            }

            log.error("Ошибка при обработке запроса:\n{}", sb);
        }

        return ioe;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        // этот метод не нужен для реализации требуемой функциональности
    }

    private void writeHeaders(Map<String, Collection<String>> headers, StringBuilder sb) {
        if (headers != null && !headers.isEmpty()) {
            String formattedHeaders = headers
                    .entrySet()
                    .stream()
                    .map(entry -> {
                        String listStr = Objects.requireNonNullElse(entry.getValue(), "").toString();
                        if (!listStr.isEmpty()) {
                            listStr = listStr.substring(1, listStr.length() - 1);
                        }
                        return String.format("\t%s: %s", entry.getKey(), listStr);
                    })
                    .collect(Collectors.joining("\n"));
            sb.append(String.format("Headers:%n%s%n", formattedHeaders));
        } else {
            sb.append("Headers: null\n");
        }
    }

    private void writeParameters(Request request, StringBuilder sb) {
        Set<Map.Entry<String, Collection<String>>> entrySet = request.requestTemplate()
                .queries()
                .entrySet();
        if (!entrySet.isEmpty()) {
            String queryParams = entrySet.stream()
                    .map(entry -> {
                        String listStr = Objects.requireNonNullElse(entry.getValue(), "").toString();
                        if (!listStr.isEmpty() && entry.getValue().size() == 1) {
                            listStr = listStr.substring(1, listStr.length() - 1);
                        }
                        return String.format("\t%s=%s", entry.getKey(), listStr);
                    })
                    .collect(Collectors.joining("\n"));
            sb.append(String.format("Parameters:%n%s%n", queryParams));
        } else {
            sb.append("Parameters: null\n");
        }
    }

    private void writeBody(byte[] bodyData, StringBuilder sb) {
        try {
            String body = objectMapper.readTree(bodyData).toPrettyString();
            sb.append(String.format("Body:%n%s%n", body));
        } catch (JsonParseException | IllegalArgumentException e) {
            byte[] checkedBodyData = Objects.requireNonNullElse(bodyData, "null".getBytes());
            sb.append(String.format("Body: %s%n", new String(checkedBodyData)));
        } catch (IOException e) {
            throw new AutotestException(e);
        }
    }
}
