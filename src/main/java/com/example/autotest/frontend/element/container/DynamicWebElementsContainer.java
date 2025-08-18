package com.example.autotest.frontend.element.container;

import com.codeborne.selenide.SelenideElement;
import com.example.autotest.configuration.settings.ApplicationSettings;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.By;
import org.slf4j.LoggerFactory;
import com.example.autotest.frontend.AbstractRegularWebElement;
import com.example.autotest.frontend.element.RegularWebElement;
import com.example.autotest.frontend.element.WebElementType;
import com.example.autotest.support.frontend.WebElementUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

public abstract class DynamicWebElementsContainer<T extends RegularWebElement> extends AbstractRegularWebElement implements WebElementContainer {

    private final By componentBaseLocator;
    private final BiFunction<Map.Entry<By, SelenideElement>, Integer, T> componentMapper;

    protected DynamicWebElementsContainer(By locator,
                                          WebElementType webElementType,
                                          String name,
                                          Integer index,
                                          By componentBaseLocator,
                                          BiFunction<Map.Entry<By, SelenideElement>, Integer, T> componentMapper) {
        super(locator, webElementType, name, index);
        this.componentBaseLocator = componentBaseLocator;
        this.componentMapper = componentMapper;
    }

    protected DynamicWebElementsContainer(By locator,
                                          WebElementType webElementType,
                                          String name,
                                          By componentBaseLocator,
                                          BiFunction<Map.Entry<By, SelenideElement>, Integer, T> componentMapper) {
        this(locator, webElementType, name, null, componentBaseLocator, componentMapper);
    }

    protected DynamicWebElementsContainer(By locator,
                                          WebElementType webElementType,
                                          Integer index,
                                          By componentBaseLocator,
                                          BiFunction<Map.Entry<By, SelenideElement>, Integer, T> componentMapper) {
        this(locator, webElementType, String.valueOf(index), index, componentBaseLocator, componentMapper);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        DynamicWebElementsContainer<?> that = (DynamicWebElementsContainer<?>) o;

        return componentBaseLocator.equals(that.componentBaseLocator) && componentMapper.equals(that.componentMapper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), componentBaseLocator, componentMapper);
    }

    protected List<T> getComponents() {
        List<T> components = Collections.emptyList();
        try {
            components = Awaitility.given()
                    .pollInSameThread()
                    .timeout(ApplicationSettings.FRONTEND_WAIT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .pollInterval(ApplicationSettings.FRONTEND_WAIT_INTERVAL, TimeUnit.MILLISECONDS)
                    .alias(String.format("Ожидание появления компонентов для элемента '%s'", this))
                    .until(() -> WebElementUtils.generateComponents(this, componentBaseLocator, componentMapper), list -> !list.isEmpty());
        } catch (ConditionTimeoutException e) {
            LoggerFactory.getLogger(this.getClass()).warn("Список компонентов пуст для элемента {}", this);
        }
        return components;
    }
}
