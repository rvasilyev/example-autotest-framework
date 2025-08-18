package com.example.autotest.test.extension;

import com.example.autotest.test.annotation.DependsOn;
import io.qameta.allure.Allure;
import io.qameta.allure.junitplatform.AllureJunitPlatform;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.support.HierarchyTraversalMode;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.commons.util.AnnotationUtils;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.MethodSelector;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.opentest4j.TestAbortedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.autotest.configuration.settings.ApplicationSettings;
import com.example.autotest.exception.AutotestException;
import com.example.autotest.support.ExtensionUtils;
import com.example.autotest.test.TestResult;
import com.example.autotest.test.TestState;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TODO: реализовать остановку запуска мастер-тестов, если среди них в процессе появляются неуспешные/отключенные, чтобы не ждать пробега оставшихся
//TODO: адаптировать решение под динамические тесты (@TestFactory). Продумать адаптацию под любой @TestTemplate-тест
public final class DependentTestExtension implements InvocationInterceptor, BeforeEachCallback, AfterEachCallback {

    private static final Logger LOG = LoggerFactory.getLogger(DependentTestExtension.class);

    private static final Map<Method, Map<Integer, TestData>> executionInfo = new ConcurrentHashMap<>();
    private static final Map<MethodSelector, Set<MethodSelector>> dependencyInfo = new ConcurrentHashMap<>();

    @Override
    public void beforeEach(ExtensionContext context) {
        ExtensionUtils.processFixture(AllureJunitPlatform.PREPARE,
                () -> LOG.info("\n*******************Старт теста {}*******************", getFullyQualifiedMethodName(context)), context);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        ExtensionUtils.processFixture(AllureJunitPlatform.TEAR_DOWN,
                () -> LOG.info("\n*******************Завершение теста {}*******************", getFullyQualifiedMethodName(context)), context);
    }

    @Override
    public void interceptTestTemplateMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        processInvocation(invocation, extensionContext, true);
    }

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        processInvocation(invocation, extensionContext, false);
    }

    private void processInvocation(Invocation<Void> invocation, ExtensionContext extensionContext, boolean isTestTemplate) throws Throwable {
        Method invokedTest = extensionContext.getRequiredTestMethod();
        Class<?> testClass = extensionContext.getRequiredTestClass();
        String testName = getFullyQualifiedMethodName(extensionContext);

        if (isTestTemplate) {
            prepareTestTemplate(extensionContext, testName);
        }

        boolean isTestRunning = false;
        synchronized (executionInfo) {
            if (!executionInfo.containsKey(invokedTest)) {
                Map<Integer, TestData> invocations = new ConcurrentHashMap<>();
                invocations.put(1, new TestData().setState(TestState.RUNNING).setResult(TestResult.UNKNOWN).setName(testName));
                executionInfo.put(invokedTest, invocations);
            } else if (executionInfo.get(invokedTest).entrySet().stream().anyMatch(entry -> testName.equals(entry.getValue().getName()))) {
                isTestRunning = true;
            } else {
                executionInfo.get(invokedTest)
                        .entrySet()
                        .stream()
                        .filter(entry -> TestState.SCHEDULED.equals(entry.getValue().getState()))
                        .findFirst()
                        .ifPresent(entry -> entry.getValue().setState(TestState.RUNNING).setName(testName));
            }
        }

        if (!isTestRunning) {
            invokeTestWithMasterTests(invocation, DiscoverySelectors.selectMethod(testClass, invokedTest), testName);
        } else {
            skipRedundantInvocation(invocation, invokedTest, testName);
        }
    }

    private void skipRedundantInvocation(Invocation<Void> invocation, Method invokedTest, String testName) throws Throwable {
        LOG.info("Тест {} уже был запущен ранее. Результат будет взят из основного запуска.", testName);
        throwFailureIfPresent(invokedTest, testName);
        getWaitSettings().alias("Ожидание выполнения основного теста при повторном запуске для " + testName)
                .until(
                        () -> {
                            Map<Integer, TestData> launches;
                            synchronized (executionInfo) {
                                launches = executionInfo.get(invokedTest);
                            }
                            return launches.entrySet()
                                    .stream()
                                    .anyMatch(entry -> testName.equals(entry.getValue().getName())
                                            && TestState.COMPLETED.equals(entry.getValue().getState()));

                        }
                );
        throwFailureIfPresent(invokedTest, testName);
        invocation.skip();
    }

    private void invokeTestWithMasterTests(Invocation<Void> invocation, MethodSelector testSelector, String testName) throws Throwable {
        Method invokedTest = testSelector.getJavaMethod();
        /*
        Сохраняем uuid теста для повторного старта записи в отчет Allure, т.к. в однопоточном режиме логирования
        тела теста не происходит из-за очистки контекста потока перед запуском каждого мастер-теста
         */
        String testUuid = Allure.getLifecycle().getCurrentTestCase().orElse("null");
        try {
            invokeMasterTests(testSelector, testName, testUuid, invokedTest);
            invokeTest(invocation, testName, testUuid, invokedTest);
        } finally {
            LOG.info("Данные о тесте: {}", getTestData(invokedTest, testName));
            LOG.debug("Информация о запуске:\n{}", executionInfo);
            LOG.debug("Информация о зависимых тестах:\n{}", dependencyInfo);
        }
    }

    private void invokeMasterTests(MethodSelector testSelector, String testName, String testUuid, Method invokedTest) {
        try {
            Set<MethodSelector> masterTests = getMasterTests(testSelector);
            synchronized (dependencyInfo) {
                String circularDependency = new CircularDependencyFinder().getCircularDependency(testSelector, dependencyInfo);
                if (circularDependency != null) {
                    throw new AutotestException(String.format("Тестовый метод %s содержит циклическую зависимость в стеке вызова:%n%s", testName, circularDependency));
                }
            }
            runMasterTests(masterTests, testName);
            waitForMasterTests(masterTests, testName);
            checkMasterTestResults(masterTests, testName);
        } catch (Throwable t) {
            Allure.getLifecycle().startTestCase(testUuid);
            if (t instanceof TestAbortedException) {
                completeTestWithResult(invokedTest, testName, TestResult.SKIPPED, t);
            } else {
                completeTestWithResult(invokedTest, testName, TestResult.BROKEN, t);
            }
            throw t;
        }
    }

    private void invokeTest(Invocation<Void> invocation, String testName, String testUuid, Method invokedTest) throws Throwable {
        try {
            Allure.getLifecycle().startTestCase(testUuid);
            invocation.proceed();
            completeTestWithResult(invokedTest, testName, TestResult.PASSED, null);
        } catch (AssertionError e) {
            completeTestWithResult(invokedTest, testName, TestResult.FAILED, e);
            throw e;
        } catch (TestAbortedException e) {
            completeTestWithResult(invokedTest, testName, TestResult.SKIPPED, e);
            throw e;
        } catch (Throwable t) {
            completeTestWithResult(invokedTest, testName, TestResult.BROKEN, t);
            throw t;
        }
    }

    private void prepareTestTemplate(ExtensionContext extensionContext, String testName) {
        Method invokedTest = extensionContext.getRequiredTestMethod();
        int invocationCnt = 0;
        Optional<RepeatedTest> repeatedTestOptional = AnnotationUtils.findAnnotation(invokedTest, RepeatedTest.class);
        if (AnnotationUtils.findAnnotation(invokedTest, ParameterizedTest.class).isPresent()) {
            if (repeatedTestOptional.isPresent()) {
                throw new AutotestException("Недопустимое сочетание типов тестов @ParameterizedTest и @RepeatedTest для " + testName);
            }
            invocationCnt = getParameterizedTestInvocationCnt(extensionContext);
        }

        if (repeatedTestOptional.isPresent()) {
            invocationCnt = repeatedTestOptional.get().value();
        }

        Map<Integer, TestData> invocations = Stream.iterate(1, n -> n + 1)
                .limit(invocationCnt)
                .map(n -> new AbstractMap.SimpleImmutableEntry<>(n, new TestData().setState(TestState.SCHEDULED).setResult(TestResult.UNKNOWN)))
                .collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue));
        synchronized (executionInfo) {
            if (!executionInfo.containsKey(invokedTest) && !invocations.isEmpty()) {
                executionInfo.put(invokedTest, invocations);
            }
        }
    }

    @SuppressWarnings("all")
    private int getParameterizedTestInvocationCnt(ExtensionContext extensionContext) {
        Method invokedTest = extensionContext.getRequiredTestMethod();
        Predicate<Method> isAnnotationConsumerAcceptMethod = method -> method.getName().equals("accept")
                && method.getParameterCount() == 1
                && method.getParameterTypes()[0].isAnnotation();
        return AnnotationUtils.findRepeatableAnnotations(invokedTest, ArgumentsSource.class)
                .stream()
                .flatMap(argumentsSource -> {
                    try {
                        ArgumentsProvider provider = ReflectionSupport.newInstance(argumentsSource.value());
                        if (provider instanceof AnnotationConsumer<?>) {
                            Method method = ReflectionSupport.findMethods(provider.getClass(), isAnnotationConsumerAcceptMethod, HierarchyTraversalMode.BOTTOM_UP).get(0);
                            Class<? extends Annotation> annotationType = (Class<? extends Annotation>) method.getParameterTypes()[0];
                            AnnotationUtils.findAnnotation(invokedTest, annotationType)
                                    .ifPresentOrElse(
                                            ((AnnotationConsumer<Annotation>) provider)::accept,
                                            () -> {
                                                throw new AutotestException(
                                                        String.format(
                                                                "Отсутствует аннотация %s для провайдера %s",
                                                                annotationType.getName(),
                                                                provider.getClass().getName()
                                                        )
                                                );
                                            }
                                    );
                        }
                        return provider.provideArguments(extensionContext);
                    } catch (Exception e) {
                        throw new AutotestException(e);
                    }
                })
                .reduce(0, (index, arguments) -> index + 1, Integer::sum);
    }

    private void checkMasterTestResults(Set<MethodSelector> masterTests, String testName) {
        Set<Map.Entry<Method, Map<Integer, TestData>>> entries;
        synchronized (executionInfo) {
            entries = executionInfo.entrySet();
        }
        Set<Method> failedMasterTests = entries.stream()
                .filter(
                        entry -> masterTests.stream().anyMatch(selector -> selector.getJavaMethod().equals(entry.getKey()))
                                && entry.getValue().entrySet().stream().anyMatch(innerEntry -> TestState.COMPLETED.equals(innerEntry.getValue().getState())
                                && !TestResult.PASSED.equals(innerEntry.getValue().getResult()))
                )
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        if (!failedMasterTests.isEmpty()) {
            String requiredTestMethods = masterTests.stream()
                    .filter(selector -> failedMasterTests.contains(selector.getJavaMethod()))
                    .map(selector -> getFullyQualifiedMethodName(selector.getJavaClass(), selector.getJavaMethod()))
                    .collect(Collectors.joining(",\n"));
            throw new TestAbortedException(
                    String.format(
                            "Тестовый метод '%s' не был запущен, так как не был(и) выполнен(ы) успешно необходимый(е) для запуска тестовый(е) метод(ы):%n%s.",
                            testName,
                            requiredTestMethods
                    )
            );
        }
    }

    private void waitForMasterTests(Set<MethodSelector> masterTests, String testName) {
        Set<Method> masterTestMethods = masterTests.stream()
                .map(MethodSelector::getJavaMethod)
                .collect(Collectors.toSet());
        getWaitSettings().alias("Ожидание выполнение всех тестов для зависимого от них теста " + testName)
                .until(
                        () -> {
                            Set<Method> keys;
                            Set<Map.Entry<Method, Map<Integer, TestData>>> entries;
                            synchronized (executionInfo) {
                                keys = executionInfo.keySet();
                                entries = executionInfo.entrySet();
                            }
                            return keys.containsAll(masterTestMethods)
                                    && entries.stream()
                                    .filter(entry -> masterTestMethods.contains(entry.getKey()))
                                    .allMatch(
                                            entry -> entry.getValue().entrySet().stream().allMatch(
                                                    innerEntry -> TestState.COMPLETED.equals(innerEntry.getValue().getState())
                                            )
                                    );
                        }
                );
    }

    private ConditionFactory getWaitSettings() {
        return Awaitility.given()
                .pollInSameThread()
                .timeout(ApplicationSettings.DEPENDENT_TEST_WAIT_TIMEOUT, TimeUnit.SECONDS)
                .pollInterval(ApplicationSettings.DEPENDENT_TEST_WAIT_INTERVAL, TimeUnit.SECONDS);
    }

    private void runMasterTests(Set<MethodSelector> masterTests, String testName) {
        masterTests.stream()
                .filter(selector -> {
                    Method method = selector.getJavaMethod();
                    synchronized (executionInfo) {
                        return !executionInfo.containsKey(method) || executionInfo.get(method).entrySet().stream().allMatch(
                                entry -> TestState.SCHEDULED.equals(entry.getValue().getState())
                        );
                    }
                })
                .parallel()
                .forEach(masterMethodSelector -> {
                    Method masterTestMethod = masterMethodSelector.getJavaMethod();
                    String masterMethodName = getFullyQualifiedMethodName(
                            masterMethodSelector.getJavaClass(),
                            masterTestMethod
                    );

                    LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                            .selectors(masterMethodSelector)
                            .build();
                    SummaryGeneratingListener listener = new SummaryGeneratingListener();
                    Launcher launcher = LauncherFactory.create();
                    launcher.registerTestExecutionListeners(listener);
                    LOG.debug("\n-------------------\nЗапуск теста {} для зависимого от него теста {}\n-------------------", masterMethodName, testName);
                    launcher.execute(request);

                    TestExecutionSummary summary = listener.getSummary();
                    if (summary.getTestsSkippedCount() > 0 || summary.getContainersSkippedCount() > 0) {
                        checkRepeatedMasterTest(testName, masterTestMethod, masterMethodName);
                        throw new TestAbortedException(
                                String.format(
                                        "Тестовый метод '%s' не был запущен, так как необходимый для запуска тестовый метод %s отключен.",
                                        testName,
                                        masterMethodName
                                )
                        );
                    }

                    summary.getFailures()
                            .forEach(failure -> LOG.debug("", failure.getException()));
                });
    }

    private void checkRepeatedMasterTest(String testName, Method masterTestMethod, String masterMethodName) {
        Optional<RepeatedTest> repeatedTestOptional = AnnotationUtils.findAnnotation(masterTestMethod, RepeatedTest.class);
        if (repeatedTestOptional.isPresent()) {
            synchronized (executionInfo) {
                if (executionInfo.containsKey(masterTestMethod)) {
                    Map<Integer, TestData> launches = executionInfo.get(masterTestMethod);
                    int failedLaunches = launches.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue().getFailure() != null)
                            .reduce(0, (index, arguments) -> index + 1, Integer::sum);
                    if (failedLaunches >= repeatedTestOptional.get().failureThreshold()) {
                        launches.entrySet()
                                .stream()
                                .filter(entry -> TestState.SCHEDULED.equals(entry.getValue().getState()))
                                .forEach(entry -> launches.remove(entry.getKey()));
                        throw new TestAbortedException(
                                String.format(
                                        "Тестовый метод '%s' не был запущен, так как необходимый для запуска тестовый метод %s достиг лимита неуспешных пробегов.",
                                        testName,
                                        masterMethodName
                                )
                        );
                    }
                }
            }
        }
    }

    @SuppressWarnings("all")
    private void throwFailureIfPresent(Method invokedTest, String testName) throws Throwable {
        Optional<Map.Entry<Integer, TestData>> failedLaunch;
        synchronized (executionInfo) {
            failedLaunch = executionInfo.get(invokedTest)
                    .entrySet()
                    .stream()
                    .filter(entry -> testName.equals(entry.getValue().getName()) && entry.getValue().getFailure() != null)
                    .findFirst();
        }
        if (failedLaunch.isPresent()) {
            throw failedLaunch.get().getValue().getFailure();
        }
    }

    private void completeTestWithResult(Method testMethod, String testName, TestResult testResult, Throwable failure) {
        TestData testData = getTestData(testMethod, testName);
        testData.setState(TestState.COMPLETED);
        testData.setResult(testResult);
        testData.setFailure(failure);
    }

    private TestData getTestData(Method testMethod, String testName) {
        Map<Integer, TestData> launches;
        synchronized (executionInfo) {
            launches = executionInfo.get(testMethod);
        }
        return launches.entrySet()
                .stream()
                .filter(entry -> testName.equals(entry.getValue().getName()))
                .findFirst()
                .orElseThrow(() -> new AutotestException("Не найдено запусков для теста " + testName))
                .getValue();
    }

    private Set<MethodSelector> getMasterTests(MethodSelector testSelector) {
        return AnnotationUtils.findAnnotation(testSelector.getJavaMethod(), DependsOn.class)
                .map(dependsOnAnnotation -> {
                    synchronized (dependencyInfo) {
                        if (!dependencyInfo.containsKey(testSelector)) {
                            Set<MethodSelector> methodSelectors = Arrays.stream(dependsOnAnnotation.value())
                                    .map(name -> {
                                        if (name.contains("#")) {
                                            return DiscoverySelectors.selectMethod(name);
                                        } else {
                                            String nameWithoutParams = name;
                                            String params = "";
                                            if (name.contains("(")) {
                                                nameWithoutParams = name.substring(0, name.indexOf("("));
                                                params = name.substring(name.indexOf("(") + 1, name.indexOf(")"));
                                            }
                                            return DiscoverySelectors.selectMethod(testSelector.getJavaClass(), nameWithoutParams, params);
                                        }
                                    })
                                    .collect(Collectors.toSet());
                            dependencyInfo.put(testSelector, methodSelectors);
                            methodSelectors.forEach(this::getMasterTests);
                            return methodSelectors;
                        } else {
                            return dependencyInfo.get(testSelector);
                        }
                    }
                })
                .orElse(Collections.emptySet());
    }

    private String getFullyQualifiedMethodName(ExtensionContext extensionContext) {
        Class<?> testClass = extensionContext.getRequiredTestClass();
        Method testMethod = extensionContext.getRequiredTestMethod();
        return String.format("%s %s", getFullyQualifiedMethodName(testClass, testMethod), extensionContext.getDisplayName());
    }

    private static String getFullyQualifiedMethodName(Class<?> clazz, Method method) {
        String paramTypes = Arrays.stream(method.getParameterTypes())
                .map(Class::getName)
                .collect(Collectors.joining(","));
        return String.format("%s#%s(%s)", clazz.getName(), method.getName(), paramTypes);
    }

    @SuppressWarnings("all")
    private static class TestData {

        private String name;
        private TestState state;
        private TestResult result;
        private Throwable failure;

        private TestData() {
        }

        public String getName() {
            return name;
        }

        public TestData setName(String name) {
            this.name = name;
            return this;
        }

        public TestState getState() {
            return state;
        }

        public TestData setState(TestState state) {
            this.state = state;
            return this;
        }

        public TestResult getResult() {
            return result;
        }

        public TestData setResult(TestResult result) {
            this.result = result;
            return this;
        }

        public Throwable getFailure() {
            return failure;
        }

        public TestData setFailure(Throwable failure) {
            this.failure = failure;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TestData testData = (TestData) o;

            return Objects.equals(name, testData.name)
                    && Objects.equals(state, testData.state)
                    && Objects.equals(result, testData.result)
                    && Objects.equals(failure, testData.failure);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, state, result, failure);
        }

        @Override
        public String toString() {
            return String.format("TestData {%n name=%s,%n state=%s,%n result=%s,%n failure=%s%n}", name, state, result, failure);
        }
    }

    private static class CircularDependencyFinder {

        // Хранилище текущей проверяемой последовательности узлов (для обнаружения цикла)
        private final Set<MethodSelector> visitingNodes;
        // Хранилище проверенных узлов (во избежание повторных проверок)
        private final Set<MethodSelector> visitedNodes;
        // Хранилище узлов цикла
        private final List<MethodSelector> cycleNodes;

        public CircularDependencyFinder() {
            visitingNodes = new HashSet<>();
            visitedNodes = new HashSet<>();
            cycleNodes = new ArrayList<>();
        }

        private boolean hasCycle(MethodSelector node, Map<MethodSelector, Set<MethodSelector>> graph) {
            // Если узел есть в текущей последовательности, то найден цикл
            if (visitingNodes.contains(node)) {
                cycleNodes.add(node);
                return true;
            }

            // Если узел уже проверен (нет цикла), то пропускаем
            if (visitedNodes.contains(node)) {
                return false;
            }

            // Добавляем узел в текущую проверяемую последовательность
            visitingNodes.add(node);

            // Рекурсивно проверяем смежные узлы
            for (MethodSelector neighbor : graph.getOrDefault(node, Collections.emptySet())) {
                if (hasCycle(neighbor, graph)) {
                    cycleNodes.add(node);
                    return true;
                }
            }

            // Узел проверен, исключаем его из текущей последовательности
            visitingNodes.remove(node);
            visitedNodes.add(node);

            return false;
        }

        public String getCircularDependency(MethodSelector startNode, Map<MethodSelector, Set<MethodSelector>> graph) {
            if (hasCycle(startNode, graph)) {
                Collections.reverse(cycleNodes); // Переворачиваем для корректного порядка элементов
                return cycleNodes
                        .stream()
                        .map(selector -> getFullyQualifiedMethodName(selector.getJavaClass(), selector.getJavaMethod()))
                        .collect(Collectors.joining("->\n"));
            }

            return null;
        }
    }
}
