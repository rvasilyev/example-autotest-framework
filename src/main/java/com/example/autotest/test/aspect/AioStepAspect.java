package com.example.autotest.test.aspect;

import com.example.autotest.backend.rest.ExampleHeaderMap;
import com.example.autotest.backend.rest.jira.aio.dto.AioStepDto;
import com.example.autotest.backend.rest.jira.aio.type.AioTestStepType;
import com.example.autotest.test.annotation.AioIgnoredParam;
import com.example.autotest.test.annotation.AioStep;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import com.example.autotest.configuration.settings.JiraSettings;
import com.example.autotest.exception.AutotestException;
import com.example.autotest.support.AioUtils;
import com.example.autotest.support.backend.DtoUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Aspect
public final class AioStepAspect {

    private static final Logger LOG = LoggerFactory.getLogger(AioStepAspect.class);
    private static final InheritableThreadLocal<List<AioStepDto>> STEPS = new InheritableThreadLocal<>();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .findAndRegisterModules()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static void initContext() {
        STEPS.set(new ArrayList<>());
    }

    public static void clearContext() {
        STEPS.remove();
    }

    public static List<AioStepDto> getSteps() {
        return Collections.unmodifiableList(STEPS.get());
    }

    @SuppressWarnings("unused")
    @Before("call(* *(..)) && @annotation(com.example.autotest.test.annotation.AioStep)")
    public void createAioStep(JoinPoint joinPoint) {
        if (JiraSettings.AIO_CASE_CREATION) {
            Method stepMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
            AioStep aioStepAnnotation = Optional.ofNullable(AnnotationUtils.findAnnotation(stepMethod, AioStep.class))
                    .orElseThrow(() -> new AutotestException("Невозможно собрать данные для AIO: отсутствует аннотация @AioStep"));

            AioStepDto aioStepDto = new AioStepDto().setTestStepType(AioTestStepType.TEXT);

            Map<String, String> headers = new HashMap<>();
            Map<String, String> requestParams = new HashMap<>();

            String stepDescription = getStepDescription(aioStepAnnotation, headers, requestParams, joinPoint);
            aioStepDto.setStep(substituteParameters(stepDescription, joinPoint));

            aioStepDto.setTestData(getStepData(aioStepAnnotation, joinPoint, headers, requestParams));

            String expectedResult = getExpectedResult(aioStepAnnotation, joinPoint);
            aioStepDto.setExpectedResult(substituteParameters(expectedResult, joinPoint));

            if (!STEPS.get().contains(aioStepDto)) {
                STEPS.get().add(aioStepDto);
            }

            LOG.info("Получены данные для шага теста AIO: {}", aioStepDto);
        }
    }

    private String substituteParameters(String parametrizedString, JoinPoint joinPoint) {
        // не достаем имена через MethodSignature, т.к. по странным причинам они там сгенерированы в виде argN
        String[] parameterNames = Arrays.stream(((MethodSignature) joinPoint.getSignature()).getMethod().getParameters())
                .map(Parameter::getName)
                .toArray(String[]::new);
        Object[] parameterValues = joinPoint.getArgs();
        String result = parametrizedString;
        for (int i = 0; i < parameterNames.length; i++) {
            result = result.replace(String.format("{%s}", parameterNames[i]), String.valueOf(parameterValues[i]));
        }

        return result;
    }

    private String getExpectedResult(AioStep aioStepAnnotation, JoinPoint joinPoint) {
        String expectedResult = substituteParameters(aioStepAnnotation.expectedResult(), joinPoint);
        try {
            Matcher matcher = Pattern.compile("(\\{[^.]+\\..+})").matcher(expectedResult);
            StringBuilder sb = new StringBuilder();
            while (matcher.find()) {
                String classSubstitutor = matcher.group(1);
                String className = classSubstitutor.substring(1, classSubstitutor.length() - 1);
                String jsonExample = String.valueOf(OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(AioUtils.createObject(className)));
                matcher.appendReplacement(sb, Matcher.quoteReplacement(jsonExample));
            }
            matcher.appendTail(sb);
            return sb.toString();
        } catch (Exception e) {
            throw new AutotestException(e);
        }
    }

    @SuppressWarnings("all")
    private String getStepData(AioStep aioStepAnnotation, JoinPoint joinPoint, Map<String, String> headers, Map<String, String> requestParams) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method stepMethod = methodSignature.getMethod();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
        StringBuilder stepData = new StringBuilder(aioStepAnnotation.data());
        RequestLine requestLineAnnotation = AnnotationUtils.getAnnotation(stepMethod, RequestLine.class);
        List<Parameter> parameters = Arrays.stream(stepMethod.getParameters())
                .filter(parameter -> AnnotationUtils.getAnnotation(parameter, AioIgnoredParam.class) == null)
                .collect(Collectors.toList());
        for (int i = 0; i < parameters.size(); i++) {
            Parameter parameter = parameters.get(i);
            Object paramValue = paramValues[i];
            if (mustBePopulated(parameter)) {
                if (Map.class.isAssignableFrom(parameter.getType())) {
                    boolean isRequestHeader = parameter.isAnnotationPresent(HeaderMap.class) || parameter.isAnnotationPresent(RequestHeader.class);
                    Map<String, String> data = isRequestHeader ? headers : requestParams;
                    data.putAll(((Map<String, String>) paramValue));
                } else {
                    Map<String, String> data = parameter.isAnnotationPresent(ExampleHeaderMap.class) ? headers : requestParams;
                    Arrays.stream(DtoUtils.extractParametersAsString(parameter.toString()).split(","))
                            .map(str -> str.trim().split("="))
                            .forEach(pair -> data.put(pair[0].trim(), pair[1].trim()));
                }
            } else if (hasRestAnnotations(parameter)) {
                try {
                    storeRequestData(headers, requestParams, parameter, paramValue, requestLineAnnotation);
                    if (AnnotationUtils.getAnnotation(parameter, RequestBody.class) != null) {
                        stepData.append("Body:\n")
                                .append(OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(paramValue))
                                .append("\n\n");
                    }
                } catch (Exception e) {
                    throw new AutotestException(e);
                }
            } else {
                stepData.append(parameterNames[i]).append("=").append(paramValue).append("\n");
            }
        }
        appendHeadersAndParams(stepData, headers, requestParams);

        return stepData.toString().trim();
    }

    private void appendHeadersAndParams(StringBuilder stringBuilder, Map<String, String> headers, Map<String, String> requestParams) {
        if (!headers.isEmpty()) {
            stringBuilder.append(convertMapToString("Headers:", headers)).append("\n\n");
        }
        if (!requestParams.isEmpty()) {
            stringBuilder.append(convertMapToString("Request parameters:", requestParams)).append("\n\n");
        }
    }

    @SuppressWarnings("all")
    private void storeRequestData(Map<String, String> headers, Map<String, String> requestParams, Parameter parameter, Object paramValue, RequestLine requestLineAnnotation) throws Exception {
        Set<Annotation> annotations = AnnotatedElementUtils.getAllMergedAnnotations(parameter, Set.of(RequestHeader.class, RequestParam.class, Param.class));
        for (Annotation annotation : annotations) {
            Class<?> annotationClass = annotation.annotationType();
            Map<String, String> data = annotationClass.equals(RequestHeader.class) ? headers : requestParams;
            if (annotationClass.equals(Param.class) && requestLineAnnotation != null) {
                String annotationValue = (String) annotationClass.getMethod("value").invoke(annotation);
                String[] params = requestLineAnnotation.value().split("\\?")[1].split("&");
                Arrays.stream(params)
                        .filter(param -> param.endsWith(annotationValue.isEmpty() ? parameter.getName() : annotationValue))
                        .map(param -> param.split("="))
                        .forEach(paramParts -> data.put(paramParts[0], paramValue.toString()));
            } else {
                String name = (String) annotationClass.getMethod("value").invoke(annotation);
                boolean required = (boolean) annotationClass.getMethod("required").invoke(annotation);
                if (name.isBlank() && required) {
                    String defaultName = (String) annotationClass.getMethod("defaultValue").invoke(annotation);
                    data.put(defaultName, paramValue.toString());
                } else {
                    data.put(name, paramValue.toString());
                }
            }
        }
    }

    private String getStepDescription(AioStep aioStepAnnotation, Map<String, String> headers, Map<String, String> requestParams, JoinPoint joinPoint) {
        String stepDescription = substituteParameters(aioStepAnnotation.description(), joinPoint);
        StringBuilder uriPrefix = new StringBuilder();
        Method stepMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        FeignClient feignClientAnnotation = AnnotationUtils.getAnnotation(stepMethod.getClass(), FeignClient.class);
        if (feignClientAnnotation != null) {
            uriPrefix.append(feignClientAnnotation.path());
        }

        RequestMapping requestMappingAnnotation = AnnotatedElementUtils.getMergedAnnotation(stepMethod, RequestMapping.class);
        if (requestMappingAnnotation != null) {
            String httpMethod = Arrays.stream(requestMappingAnnotation.method())
                    .map(Enum::name)
                    .collect(Collectors.joining(","));
            String uriPath = Arrays.stream(requestMappingAnnotation.path())
                    .map(uriPrefix::append)
                    .collect(Collectors.joining(","));
            stepDescription = processStepDescription(stepDescription, httpMethod, uriPath);

            convertStringArrayToMap(requestMappingAnnotation.headers(), headers);
            convertStringArrayToMap(requestMappingAnnotation.params(), requestParams);
        }

        RequestLine requestLineAnnotation = AnnotationUtils.getAnnotation(stepMethod, RequestLine.class);
        if (requestLineAnnotation != null) {
            String[] splitValue = requestLineAnnotation.value().split(" ");
            String uriPath = uriPrefix.append(splitValue[1].split("\\?")[0]).toString();
            stepDescription = processStepDescription(stepDescription, splitValue[0], uriPath);
        }

        Headers headersAnnotation = AnnotationUtils.getAnnotation(stepMethod, Headers.class);
        if (headersAnnotation != null) {
            Arrays.stream(headersAnnotation.value())
                    .map(header -> header.split(":"))
                    .forEach(headerParts -> headers.put(headerParts[0].trim(), headerParts[1].trim()));
        }

        return stepDescription;
    }

    private String processStepDescription(String stepDescription, String httpMethod, String uriPath) {
        return stepDescription.replace("{httpMethod}", httpMethod).replace("{resourcePath}", uriPath);
    }

    private void convertStringArrayToMap(String[] data, Map<String, String> map) {
        Arrays.stream(data)
                .filter(value -> !value.substring(0, value.indexOf("=")).contains("!"))
                .forEach(value -> {
                    String[] valueParts = value.split("=");
                    map.put(valueParts[0], valueParts[1]);
                });
    }

    private boolean mustBePopulated(Parameter parameter) {
        Predicate<Class<? extends Annotation>> predicate = annotationClass -> Optional.ofNullable(AnnotatedElementUtils.getMergedAnnotation(parameter, annotationClass))
                .map(annotation -> {
                    try {
                        String value = (String) annotation.annotationType().getMethod("value").invoke(annotation);
                        String defaultValue = (String) annotation.annotationType().getMethod("defaultValue").invoke(annotation);
                        return !value.isBlank() || !defaultValue.equals(ValueConstants.DEFAULT_NONE);
                    } catch (Exception e) {
                        throw new AutotestException(e);
                    }
                })
                .orElse(true);
        boolean requestHeaderSpecified = predicate.test(RequestHeader.class);
        boolean requestParamSpecified = predicate.test(RequestParam.class);
        boolean isMap = Map.class.isAssignableFrom(parameter.getType());

        return parameter.isAnnotationPresent(SpringQueryMap.class)
                || parameter.isAnnotationPresent(ExampleHeaderMap.class)
                || (isMap && parameter.isAnnotationPresent(HeaderMap.class))
                || (isMap && !requestHeaderSpecified)
                || (isMap && !requestParamSpecified);
    }

    private boolean hasRestAnnotations(Parameter parameter) {
        return !AnnotatedElementUtils.getAllMergedAnnotations(parameter,
                        Set.of(RequestHeader.class, RequestParam.class, Param.class, RequestBody.class))
                .isEmpty();
    }

    private String convertMapToString(String sectionName, Map<String, String> data) {
        StringBuilder stringBuilder = new StringBuilder(sectionName).append("\n");
        String dataString = data.entrySet()
                .stream()
                .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
        return stringBuilder.append(dataString).append("\n").toString();
    }
}
